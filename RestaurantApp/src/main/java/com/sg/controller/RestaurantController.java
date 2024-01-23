package com.sg.controller;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sg.service.RestaurantService;

@RestController
@RequestMapping(path = "restaurant")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@PostMapping(value = "/startSession")
	public ResponseEntity<String> startSession(@RequestParam String username){
		String session = restaurantService.startSession(username);
		return new ResponseEntity<>(Base64.getEncoder().encodeToString(session.getBytes()), HttpStatus.OK);
	}
	
	@PostMapping(value = "/joinSession")
	public ResponseEntity<List<String>> joinSession(@RequestParam String sessionId){
		try {
			byte[] decoded = Base64.getDecoder().decode(sessionId);
			List<String> rest = restaurantService.joinSession(new String(decoded, "UTF-8"));
			return new ResponseEntity<>(rest, HttpStatus.OK);
		}catch(IllegalArgumentException | UnsupportedEncodingException ex) {
			if(ex.getMessage().contains("not found")) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
	}
	
	@PostMapping(value = "/endSession")
	public ResponseEntity<String> endSession(@RequestParam String username, @RequestParam String sessionId){
		try {
			byte[] decoded = Base64.getDecoder().decode(sessionId);
			String finalRest = restaurantService.endSession(username, new String(decoded, "UTF-8"));
			return new ResponseEntity<>(finalRest, HttpStatus.OK);
		}catch(IllegalArgumentException | UnsupportedEncodingException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@PostMapping(value = "/submitRestaurant")
	public ResponseEntity<List<String>> submitRestaurant(@RequestParam String sessionId, @RequestParam String restaurantName){
		try {
			byte[] decoded = Base64.getDecoder().decode(sessionId);
			List<String> resLst = restaurantService.submitRestaurant(new String(decoded, "UTF-8"), restaurantName);
			return new ResponseEntity<>(resLst, HttpStatus.OK);
		}catch(IllegalArgumentException | UnsupportedEncodingException ex) {
			if(ex.getMessage().contains("not found")) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@GetMapping(value = "/getRestaurants")
	public ResponseEntity<List<String>> getRestaurants(@RequestParam String sessionId){
		try {
			byte[] decoded = Base64.getDecoder().decode(sessionId);
			List<String> resLst = restaurantService.getRestaurants(new String(decoded, "UTF-8"));
			return new ResponseEntity<>(resLst, HttpStatus.OK);
		}catch(IllegalArgumentException | UnsupportedEncodingException ex) {
			if(ex.getMessage().contains("not found")) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@GetMapping(value = "/getSelectedRestaurant")
	public ResponseEntity<String> getSelectedRestaurant(@RequestParam String sessionId){
		try {
			byte[] decoded = Base64.getDecoder().decode(sessionId);
			String rest = restaurantService.getSelectedRestaurant(new String(decoded, "UTF-8"));
			return new ResponseEntity<>(rest, HttpStatus.OK);
		}catch(IllegalArgumentException | UnsupportedEncodingException ex) {
			if(ex.getMessage().contains("not found")) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
}
