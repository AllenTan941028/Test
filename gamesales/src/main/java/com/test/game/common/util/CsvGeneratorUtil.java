package com.test.game.common.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class CsvGeneratorUtil {

	private static final String CSV_HEADER = "ID,Game No,Game Name,Game Code,Type,Cost Price,Tax,Sale Price,Date Of Sale";
	private static final String[] CHARACTERS = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	private static final String[] GAME_NAMES = {"GTA", "Zelda", "ResidentEvil", "Super Mario", "Red Dead", "Half-Life", "God Of War", "Fortnite", "Left4Dead", "Minecraft"};
	private SecureRandom random = new SecureRandom();
    public String generateCsv(int noOfRecords) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER).append("\n");

        for(int i=0; i<noOfRecords; i++) {
        	double cost = this.getRandomCost();
        	int tax = 9;
        	double salePrice = cost * (100 + tax)/100;
        	csvContent.append(i+1).append(",")//id
        	.append(this.getRandomGameNo()).append(",")//Game No
        	.append(this.getRandomGameName()).append(",")//Game Name
        	.append(this.getRandomGameCode()).append(",")//Game Code
        	.append(this.getRandomType()).append(",")//Type
        	.append(cost).append(",")//Cost Price
        	.append(tax).append(",")//Tax
        	.append(salePrice).append(",")//Sale Price
        	.append("2024-04-" + this.getRandomDay() + " 00:00:00").append("\n");//Date Of Sale
        }

        return csvContent.toString();
    }
    
    private int getRandomGameNo() {
    	
    	return random.nextInt(1,101);
    }
    
    private String getRandomGameName() {
    	return GAME_NAMES[random.nextInt(10)];
    }
    
    private String getRandomGameCode() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(CHARACTERS[random.nextInt(26)])
    	.append(CHARACTERS[random.nextInt(26)])
    	.append(CHARACTERS[random.nextInt(26)])
    	.append(CHARACTERS[random.nextInt(26)])
    	.append(CHARACTERS[random.nextInt(26)]);
    	
    	return sb.toString();
    }
    
    private int getRandomType() {
    	return random.nextInt(2) + 1;
    }
    
    private double getRandomCost() {
    	return random.nextInt(1, 101);
    }
    
    private String getRandomDay() {
    	String day = String.valueOf(random.nextInt(1,31));
    	return day.length() == 1 ? "0" + day : day;
    }
}
