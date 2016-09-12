package com.nexmo.hangman.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexmo.hangman.manager.GameManager;
import com.nexmo.hangman.model.GameSession;
import com.nexmo.hangman.model.InGameSession;
import com.nexmo.hangman.service.GameService;

@Service("gameService")
public class GameServiceImpl implements GameService {

	@Autowired
	private GameManager gameManager;

	@Override
	public InGameSession retrieveExistingGameSession (String ipAddress) {
		GameSession gameSession = this.gameManager.getGameSession(ipAddress);
		return createInGameSession(gameSession);
	}

	@Override
	public GameSession getGameSession(String ipAddress) {
		return this.gameManager.getGameSession(ipAddress);
	}

	@Override
	public InGameSession makeAttempt(String ipAddress, String letter) throws Exception{
		boolean success = this.gameManager.makeAttempt(ipAddress, letter);
		GameSession gameSession = getGameSession(ipAddress);
		InGameSession inGameSession = createInGameSession(gameSession); 
		if(inGameSession != null){
			inGameSession.setSuccessfulAttempt(success);
		}
		return inGameSession;
	}

	@Override
	public InGameSession createGameSession(String ipAddress, String userName) {
		GameSession gameSession = this.gameManager.createGameSession(ipAddress, userName);
		return createInGameSession(gameSession);
	}

	private InGameSession createInGameSession(GameSession gameSession){
		InGameSession inGameSession = null;
		if(gameSession!= null){
			inGameSession = new InGameSession();
			inGameSession.setUser(gameSession.getUser());
			inGameSession.setWordID(gameSession.getWordID());
			inGameSession.setDummyWord(gameSession.getDummyWord());
			inGameSession.setCurrentGameState(gameSession.getCurrentGameState());
			inGameSession.setNumberOfRemainingAttempts(gameSession.getNumberOfRemainingAttempts());
			inGameSession.setSuccessfulLetters(gameSession.getSuccessfulLetters());
			inGameSession.setUnSuccessfulLetters(gameSession.getUnSuccessfulLetters());
		}
		return inGameSession;
	}
}
