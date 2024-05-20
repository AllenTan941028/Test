package com.test.game.common.view;

import java.io.Serializable;
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
@Table(name = "game_sales")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GameView implements Serializable {
	private static final long serialVersionUID = 1837467094166030958L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private int id;
	
	@Column(name = "game_no")
	private int gameNo;
	
	@Column(name = "game_name")
	private String gameName;
	
	@Column(name = "game_code")
	private String gameCode;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "cost_price")
	private double costPrice;
	
	@Column(name = "tax")
	private int tax;
	
	@Column(name = "sale_price")
	private double salePrice;
	
	@Column(name = "date_of_sale")
	private LocalDateTime dateOfSale;

	
	
	public GameView() {
		super();
	}

	public GameView(int id, int gameNo, String gameName, String gameCode, int type, double costPrice, int tax,
			double salePrice, LocalDateTime dateOfSale) {
		super();
		this.id = id;
		this.gameNo = gameNo;
		this.gameName = gameName;
		this.gameCode = gameCode;
		this.type = type;
		this.costPrice = costPrice;
		this.tax = tax;
		this.salePrice = salePrice;
		this.dateOfSale = dateOfSale;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public LocalDateTime getDateOfSale() {
		return dateOfSale;
	}

	public void setDateOfSale(LocalDateTime dateOfSale) {
		this.dateOfSale = dateOfSale;
	}

	@Override
	public String toString() {
		return "GameView [id=" + id + ", gameNo=" + gameNo + ", gameName=" + gameName + ", gameCode=" + gameCode
				+ ", type=" + type + ", costPrice=" + costPrice + ", tax=" + tax + ", salePrice=" + salePrice
				+ ", dateOfSale=" + dateOfSale + "]";
	}
}
