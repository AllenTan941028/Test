package com.sg.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.sg.model.Session;

@Service
public class RestaurantService {

	//Using concurrentHashMap get to ensure all session have its own restaurant list
	//To control concurrency issue when there are multiple user submit restaurant name in the same session
	private static final ConcurrentHashMap<String, Session> Sessions = new ConcurrentHashMap<>();
	
	public String startSession(String host) {
		String sessionId = UUID.randomUUID().toString();
		Session session = new Session(sessionId, host);
		Sessions.put(sessionId, session);
		
		return sessionId;
	}
	
	public List<String> joinSession(String sessionId) {
		Session session = null;
		if(Sessions.containsKey(sessionId)) {
			session = Sessions.get(sessionId);
		}else {
			throw new IllegalArgumentException("Session not found.");
		}
		
		if(session.isEnded()) {
			throw new IllegalArgumentException("Session has ended.");
		}
		
		return session.getRestaurantLst();
	}
	
	public String endSession(String host, String sessionId) {
		String rest = "";
		Session session = null;
		if(!Sessions.containsKey(sessionId)) {
			throw new IllegalArgumentException("Session not found.");
			
		}
		session = Sessions.get(sessionId);
		
		if(session.getInitiator().equals(host)) {
			//random select a restaurant
			if(!session.getRestaurantLst().isEmpty()) {
				if(session.getSelectedRestaurant().isEmpty()) {
					rest = session.randomPick();
				}else {
					rest = session.getSelectedRestaurant();
				}
			}else {
				rest = "No restaurant were submitted in this session.";
			}
			session.closeSession();
		}else {
			throw new IllegalArgumentException("Invalid host.");
		}
		return rest;
	}
	
	public List<String> submitRestaurant(String sessionId, String restaurant) {
		if(Sessions.containsKey(sessionId)) {
			Session session = Sessions.get(sessionId);
			if(!session.isEnded()) {
				session.addRestaurant(restaurant);
			}
			return session.getRestaurantLst();
		}else {
			throw new IllegalArgumentException("Session not found.");
		}
	}
	
	public List<String> getRestaurants(String sessionId){
		Session session = null;
		if(Sessions.containsKey(sessionId)) {
			session = Sessions.get(sessionId);
		}else {
			throw new IllegalArgumentException("Session not found.");
		}
		
		if(session.isEnded()) {
			throw new IllegalArgumentException("Session has ended.");
		}
		
		return session.getRestaurantLst();
	}
	
	public String getSelectedRestaurant(String sessionId) {
		Session session = null;
		if(Sessions.containsKey(sessionId)) {
			session = Sessions.get(sessionId);
		}else {
			throw new IllegalArgumentException("Session not found.");
		}
		
		return session.getSelectedRestaurant();
	}
}
