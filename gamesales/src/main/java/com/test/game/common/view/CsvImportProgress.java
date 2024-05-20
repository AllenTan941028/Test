package com.test.game.common.view;

import java.time.LocalDateTime;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "import_progress")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CsvImportProgress {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "total_records")
	private int totalRecords;
	@Column(name = "processed_records")
	private int processedRecords;
	@Column(name = "invalid_records")
	private int invalidRecords;
	@Column(name = "start_time")
	private LocalDateTime startTime;
	@Column(name = "end_time")
	private LocalDateTime endTime;
	@Column(name = "process_time")
	private String processTime;
	@Column(name = "status")
	private String status;
	@Column(name = "message")
	private String message;
	
	public CsvImportProgress() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getProcessedRecords() {
		return processedRecords;
	}

	public void setProcessedRecords(int processedRecords) {
		this.processedRecords = processedRecords;
	}
	
	public int getInvalidRecords() {
		return invalidRecords;
	}

	public void setInvalidRecords(int invalidRecords) {
		this.invalidRecords = invalidRecords;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CsvImportProgress [id=" + id + ", totalRecords=" + totalRecords + ", processedRecords="
				+ processedRecords + ", invalidRecords=" + invalidRecords + ", startTime=" + startTime + ", endTime="
				+ endTime + ", processTime=" + processTime + ", status=" + status + ", message=" + message + "]";
	}
	
	
}
