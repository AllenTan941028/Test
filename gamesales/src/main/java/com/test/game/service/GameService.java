package com.test.game.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.test.game.common.model.CSVUploadResponse;
import com.test.game.common.model.GameSalesResponse;
import com.test.game.common.repo.GameSalesBatchInsertDao;
import com.test.game.common.repo.ICsvImportProgressCRUDRepository;
import com.test.game.common.repo.IGameRepository;
import com.test.game.common.view.CsvImportProgress;
import com.test.game.common.view.GameView;

@Service
public class GameService  implements InitializingBean {

	private static final int BATCH_SIZE = 10000;
	
	private static final List<GameView> gameLst = Collections.synchronizedList(new ArrayList<GameView>());
	private static final String DATE_EXCEPTION_MSG = "Invalid Date only allow format 'yyyy-MM-dd'. Exception : ";
	
	@Autowired
	private IGameRepository gameCRUDRepository;

	@Autowired
	private ICsvImportProgressCRUDRepository csvImportProgressCRUDRepository;

	@Autowired
	private GameSalesBatchInsertDao batchInsertDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		gameLst.addAll(gameCRUDRepository.findAll());
	}

	public void getOverallGameSales(List<GameView> gameList, String fromDate, String toDate, Integer targetSalePrice, int page, int size, final GameSalesResponse response) {
		try {
			List<GameView> result = gameLst.stream()
					.filter(game -> {
						boolean matches = true;
						if(fromDate != null && !fromDate.isEmpty()) {
							matches = matches && game.getDateOfSale().equals(this.getFromDateTime(fromDate)) || game.getDateOfSale().isAfter(this.getFromDateTime(fromDate));
						}
						
						if(toDate != null && !toDate.isEmpty()) {
							matches = matches && game.getDateOfSale().equals(this.getToDateTime(toDate)) || game.getDateOfSale().isBefore(this.getToDateTime(toDate));
						}
						
						if(targetSalePrice != null && targetSalePrice > 0) {
							matches = matches && Double.compare(game.getSalePrice(), targetSalePrice) == 0 || Double.compare(game.getSalePrice(), targetSalePrice) > 0;
						}
						
						return matches;
					})
					.skip(page * size)
					.limit(size)
					.collect(Collectors.toList());
			
			gameList.addAll(result);
			
			if(result.isEmpty()) {
				response.setResponseMessage("No Result selected.");
			}
		} catch (DateTimeParseException e) {
			response.setResponseMessage(DATE_EXCEPTION_MSG + e.getMessage());
		}
	}
	
	public void getCountGamesSoldInBetween(String fromDate, String toDate, StringBuilder sb) {
		try {
			LocalDateTime fDateTime = this.getFromDateTime(fromDate);
			LocalDateTime tDateTime = this.getToDateTime(toDate);
			
			long count = gameLst.stream()
	                .filter(sale -> (sale.getDateOfSale().isEqual(fDateTime) || sale.getDateOfSale().isAfter(fDateTime)) && (sale.getDateOfSale().isBefore(tDateTime) || sale.getDateOfSale().isEqual(tDateTime)))
	                .map(GameView::getGameNo)
	                .count();
			
			sb.append("Total no of games sold : ").append(count);
		} catch (DateTimeParseException e) {
			sb.append(DATE_EXCEPTION_MSG + e.getMessage());
		}
	}
	
	public void getTotalSalesOfGameInBetween(String fromDate, String toDate, String gameNo, StringBuilder sb) {
		try {
			LocalDateTime fDateTime = this.getFromDateTime(fromDate);
			LocalDateTime tDateTime = this.getToDateTime(toDate);
			
			double totalSalesPrice = gameLst.stream()
	                .filter(sale -> {
	                	boolean matches = true;
	                	if(gameNo != null && !gameNo.isEmpty()) {
	                		matches = matches && sale.getGameNo() == Integer.valueOf(gameNo);
	                	}
	                	
	                	if(fromDate != null && !fromDate.isEmpty()) {
	                		matches = matches && (sale.getDateOfSale().isEqual(fDateTime) || sale.getDateOfSale().isAfter(fDateTime));
	                	}
	                	
	                	if(toDate != null && !toDate.isEmpty()) {
	                		matches = matches && (sale.getDateOfSale().isBefore(tDateTime) || sale.getDateOfSale().isEqual(tDateTime));
	                	}
	                	
	                	return matches;
	                })
	                .mapToDouble(GameView::getSalePrice)
	                .sum();
			
			if(gameNo != null && !gameNo.isEmpty()) {
				sb.append("Game No : ").append(gameNo)
				.append(" Total Sales : ").append(String.format("%.2f", totalSalesPrice));
			}else {
				sb.append("All Games Total Sales : ").append(String.format("%.2f", totalSalesPrice));
			}
			
		} catch (DateTimeParseException e) {
			sb.append(DATE_EXCEPTION_MSG + e.getMessage());
		}
	}

	@Transactional
	public CSVUploadResponse importCsvfile(final MultipartFile file, final CSVUploadResponse response) {

		if (file == null) {
			this.generateErrorResponse("File is null.", response);
			return response;
		}

		int totalNoOfRecords = 0;

		try {
			totalNoOfRecords = this.getTotalRecordsInCsvFile(file);
		} catch (IOException e1) {
			this.generateErrorResponse(e1.getMessage(), response);
			return response;
		}
		
		if(totalNoOfRecords <= 0) {
			this.generateErrorResponse("No record found in File to import.", response);
			return response;
		}
		
		gameLst.clear();
		Long startTimeMilli = System.currentTimeMillis();// record start time milli
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		System.out.println("Import started");
		// Create CsvImportProgress and save to DB
		CsvImportProgress progress = new CsvImportProgress();
		progress.setTotalRecords(totalNoOfRecords);
		this.createCSVImportProgressInDB(progress);
		
		// Use StringBuffer to keep track of invalid records
		StringBuffer invalidMessages = new StringBuffer();
		// Use AtomicInteger to count total invalid records
		AtomicInteger countInvalid = new AtomicInteger();
		// Use AtomicInteger to count total processed records
		AtomicInteger countProcess = new AtomicInteger();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				CSVReader csvReader = new CSVReader(reader)) {
			String[] values;
			List<GameView> gameViews = new ArrayList<>();

			// Skip header
			csvReader.readNext();

			while ((values = csvReader.readNext()) != null) {
				GameView gview = new GameView();
				try {
					gview.setId(Integer.valueOf(values[0]));
					gview.setGameNo(Integer.valueOf(values[1]));
					gview.setGameName(values[2]);
					gview.setGameCode(values[3]);
					gview.setType(Integer.valueOf(values[4]));
					gview.setCostPrice(Double.valueOf(values[5]));
					gview.setTax(Integer.valueOf(values[6]));
					gview.setSalePrice(Double.valueOf(values[7]));
					gview.setDateOfSale(this.convertStrToLocalDateTime(values[8]));
					gameViews.add(gview);
				}catch (NumberFormatException nex) {
					StringBuilder sb = new StringBuilder(progress.getMessage());
					sb.append("Record ID : " + gview.getId() + ", Exception: " + nex.getMessage()).append("\n");
					progress.setMessage(sb.toString());
					
					countInvalid.getAndIncrement();
					progress.setInvalidRecords(countInvalid.get());
					this.updateProgress(progress);
					continue;
				}
				

				if (gameViews.size() >= BATCH_SIZE) {
					List<GameView> batch = new ArrayList<>(gameViews);
					executorService.submit(() -> validateAndSaveToDB(batch, countProcess, countInvalid, invalidMessages));
					// get count and update progress table
					progress.setProcessedRecords(countProcess.get());
					progress.setInvalidRecords(countInvalid.get());
					this.updateProgress(progress);

					gameViews.clear();
				}
			}
			if (!gameViews.isEmpty()) {
				executorService.submit(() -> validateAndSaveToDB(gameViews, countProcess, countInvalid, invalidMessages));

				progress.setProcessedRecords(countProcess.get());
				progress.setInvalidRecords(countInvalid.get());
				this.updateProgress(progress);
			}
		} catch (IOException | CsvValidationException e) {
			StringBuilder sb = new StringBuilder(progress.getMessage());
			sb.append("Import interrupted by Exception: " + e.getMessage()).append("\n");
			progress.setMessage(sb.toString());
		} finally {
			executorService.shutdown();

			try {
				executorService.awaitTermination(5, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				executorService.shutdownNow();
			}

			progress.setProcessedRecords(countProcess.get());
			progress.setInvalidRecords(countInvalid.get());
			progress.setEndTime(LocalDateTime.now());
			progress.setStatus("COMPLETED");
			progress.setProcessTime(System.currentTimeMillis() - startTimeMilli + " ms");
			if(!invalidMessages.isEmpty()) {
				StringBuilder sb = new StringBuilder(progress.getMessage());
				sb.append(invalidMessages.toString());
				progress.setMessage(sb.toString());
			}
			
			if(progress.getMessage().isEmpty()) {
				progress.setMessage("CSV Import Successfully.");
			}
			this.updateProgress(progress);
		}
		System.out.println("Import ended " + gameLst.size());
		response.setTotalTimeTaken(progress.getProcessTime());
		response.setResponseMessage(progress.getMessage());
		return response;
	}

	@Transactional
	private void validateAndSaveToDB(List<GameView> games, AtomicInteger count, AtomicInteger countInvalid, StringBuffer invalidMsg) {
		List<GameView> validatedGames = new ArrayList<>();

		for (GameView g : games) {
			// validation
			if (validateCsvRecord(g)) {
				// once validated, do increment for the count
				gameLst.add(g);
				count.getAndIncrement();
				validatedGames.add(g);
			}else {
				countInvalid.getAndIncrement();
				invalidMsg.append("Invalid Record : " + g.toString()).append("\n");
			}
		}
		System.out.println("Attempt to save to DB. " + Thread.currentThread().getName());
		batchInsertDao.performBatchInsert(validatedGames);
	}

	private boolean validateCsvRecord(GameView game) {
		boolean id = (game.getId() > 0);
		boolean gameNo = (game.getGameNo() > 0 && game.getGameNo() <= 100);
		boolean gameName = (!game.getGameName().isBlank() && game.getGameName().length() <= 20);
		boolean gameCode = (!game.getGameCode().isBlank() && game.getGameCode().length() <= 5);
		boolean type = (game.getType() == 1 || game.getType() == 2);
		boolean costPrice = (game.getCostPrice() <= 100);
		boolean tax = (game.getTax() == 9);
		double calSalePrice = game.getCostPrice() * (100 + game.getTax()) / 100;
		boolean salePrice = (game.getSalePrice() == calSalePrice);
		boolean dateOfSale = (game.getDateOfSale() != null);

		return id && gameNo && gameName && gameCode && type && costPrice && tax && salePrice && dateOfSale;
	}

	@Transactional
	private void updateProgress(CsvImportProgress progress) {
		CsvImportProgress importProgress = csvImportProgressCRUDRepository.findById(progress.getId()).orElseThrow();
		importProgress.setProcessedRecords(progress.getProcessedRecords());
		importProgress.setInvalidRecords(progress.getInvalidRecords());
		importProgress.setStartTime(progress.getStartTime());
		importProgress.setEndTime(progress.getEndTime());
		importProgress.setTotalRecords(progress.getTotalRecords());
		importProgress.setStatus(progress.getStatus());
		importProgress.setProcessTime(progress.getProcessTime());
		importProgress.setMessage(progress.getMessage());
		csvImportProgressCRUDRepository.save(importProgress);
	}

	private LocalDateTime convertStrToLocalDateTime(String s) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(s, formatter);
	}

	private int getTotalRecordsInCsvFile(MultipartFile file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			return (int) (reader.lines().count() - 1);
		}
	}
	
	private void createCSVImportProgressInDB(CsvImportProgress progress) {
		progress.setStartTime(LocalDateTime.now());
		progress.setStatus("IN_PROGRESS");
		progress.setProcessedRecords(0);
		progress.setInvalidRecords(0);
		progress.setMessage("");
		csvImportProgressCRUDRepository.save(progress);
	}
	
	private void generateErrorResponse(String errorMsg, CSVUploadResponse response) {
		response.setTotalTimeTaken("0 ms");
		response.setResponseMessage(errorMsg);
	}

	private LocalDateTime getFromDateTime(String stringDate) throws DateTimeParseException {
		return this.convertStringDateToLocalDate(stringDate).atStartOfDay();
	}
	
	private LocalDateTime getToDateTime(String stringDate) {
		return this.convertStringDateToLocalDate(stringDate).atTime(23, 59, 59);
	}
	
	private LocalDate convertStringDateToLocalDate(String stringDate) throws DateTimeParseException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(stringDate, formatter);
	}
}
