package com.sg.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Session {
	private final String sessionId;
	private final String initiator;
	private List<String> restaurantLst = new ArrayList<String>();
	private boolean isEnded;
	private String selectedRestaurant;
	
	
	public Session(String sessionId, String initiator) {
		super();
		this.sessionId = sessionId;
		this.initiator = initiator;
		this.isEnded = false;
	}
	
	public List<String> getRestaurantLst() {
		return restaurantLst;
	}
	
	public void setRestaurantLst(List<String> restaurantLst) {
		this.restaurantLst = restaurantLst;
	}
	
	public boolean isEnded() {
		return isEnded;
	}
	
	public void setEnded(boolean isEnded) {
		this.isEnded = isEnded;
	}
	
	public String getInitiator() {
		return initiator;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public String getSelectedRestaurant() {
		return selectedRestaurant;
	}

	public void setSelectedRestaurant(String selectedRestaurant) {
		this.selectedRestaurant = selectedRestaurant;
	}

	public void addRestaurant(String restaurant) {
		if(!restaurantLst.contains(restaurant)) {
			restaurantLst.add(restaurant);
		}
	}
	
	public void closeSession() {
		this.isEnded = true;
	}
	
	public String randomPick() {
		Random rand = new Random();
		String restaurant = restaurantLst.get(rand.nextInt(restaurantLst.size()));
		this.selectedRestaurant = restaurant;
		return restaurant;
	}
}
