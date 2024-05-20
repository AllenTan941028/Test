package com.test.game.common.model;

import java.time.LocalDateTime;

public class RestResponse {

	private LocalDateTime requestTime;
	
	private LocalDateTime responseTime;
	
	private String totalTimeTaken;

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}

	public LocalDateTime getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(LocalDateTime responseTime) {
		this.responseTime = responseTime;
	}

	public String getTotalTimeTaken() {
		return totalTimeTaken;
	}

	public void setTotalTimeTaken(String totalTimeTaken) {
		this.totalTimeTaken = totalTimeTaken;
	}
	
	
}
