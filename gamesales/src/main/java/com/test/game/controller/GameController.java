package com.test.game.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.test.game.common.model.CSVUploadResponse;
import com.test.game.common.model.GameSalesResponse;
import com.test.game.common.model.TotalSalesResponse;
import com.test.game.common.util.CsvGeneratorUtil;
import com.test.game.common.view.GameView;
import com.test.game.service.GameService;

@RestController
@RequestMapping(path = "/game")
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private CsvGeneratorUtil csvGeneratorUtil;
	
	/**
	 * NOTE: The endpoint for importing CSV with 1 million record should complete within 1 minute
	 * @param file
	 * @return
	 */
	@PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CSVUploadResponse uploadAndImportCSV(@RequestParam(value = "file") MultipartFile file) {
		CSVUploadResponse response = new CSVUploadResponse();
		response.setRequestTime(LocalDateTime.now());
		
		gameService.importCsvfile(file, response);
		
		response.setResponseTime(LocalDateTime.now());
		return response;
	}
	
	
	
	/**
	 * NOTE: The endpoint support pagination and page size. The return results time must be less than 1 second
	 * @param fromDate
	 * @param toDate
	 * @param targetSalePrice 
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping(value = "/getGameSales")
	public GameSalesResponse getGameSales(@RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate, 
			@RequestParam(value = "salePrice", required = false) Integer targetSalePrice, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "100") int size){
		long requestTimeMilli = System.currentTimeMillis();
		GameSalesResponse response = new GameSalesResponse();
		response.setRequestTime(LocalDateTime.now());
		if(size > 100) {
			size = 100;
		}
		
		List<GameView> gameList = new ArrayList<>();
		gameService.getOverallGameSales(gameList, fromDate, toDate, targetSalePrice, page, size, response);
		
		response.setGamePage(gameList);
		response.setResponseTime(LocalDateTime.now());
		response.setTotalTimeTaken(System.currentTimeMillis() - requestTimeMilli + " ms");
		return response;
	}
	
	/**
	 * NOTE: The return results time must be less than 1 second
	 * 
	 * @param displayTotalSales TRUE/FALSE to display count/total sales of game
	 * @param fromDate
	 * @param toDate
	 * @param gameNo
	 * @return
	 */
	@GetMapping(value = "/getTotalSales")
	public TotalSalesResponse getTotalSales(@RequestParam(defaultValue = "true") boolean displayTotalSales, @RequestParam(value = "fromDate", required = false) String fromDate,  
			@RequestParam(value = "toDate", required = false) String toDate, @RequestParam(value = "game_no", required = false) String gameNo) {
		long requestTimeMilli = System.currentTimeMillis();
		TotalSalesResponse response = new TotalSalesResponse();
		response.setRequestTime(LocalDateTime.now());
		
		StringBuilder sb = new StringBuilder();
		if(!displayTotalSales) {
			gameService.getCountGamesSoldInBetween(fromDate, toDate, sb);
		}else {
			gameService.getTotalSalesOfGameInBetween(fromDate, toDate, gameNo, sb);
		}
		response.setResponseMessage(sb.toString());
		response.setResponseTime(LocalDateTime.now());
		response.setTotalTimeTaken(System.currentTimeMillis() - requestTimeMilli + " ms");
		
		return response;
	}


	@GetMapping("/csv")
    public ResponseEntity<byte[]> generateCsvFile(@RequestParam(defaultValue = "1000000") int totalOfRecords) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "gamesales.csv");

        byte[] csvBytes = csvGeneratorUtil.generateCsv(totalOfRecords).getBytes();

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

}
