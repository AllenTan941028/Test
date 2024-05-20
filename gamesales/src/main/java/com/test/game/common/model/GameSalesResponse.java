package com.test.game.common.model;

import java.util.List;

import com.test.game.common.view.GameView;

public class GameSalesResponse extends RestResponse {

	private List<GameView> gamePage;
	private String responseMessage;

	public List<GameView> getGamePage() {
		return gamePage;
	}

	public void setGamePage(List<GameView> gamePage) {
		this.gamePage = gamePage;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
}
