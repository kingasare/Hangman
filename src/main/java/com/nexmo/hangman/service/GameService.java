package com.nexmo.hangman.service;

import com.nexmo.hangman.model.GameSession;
import com.nexmo.hangman.model.InGameSession;

public interface GameService {

	InGameSession retrieveExistingGameSession(String ipAddress);
	GameSession getGameSession(String ipAddress);
	InGameSession makeAttempt(String ipAddress, String letter) throws Exception;
	InGameSession createGameSession(String ipAddress, String userName);
	boolean checkGameSessionExists(String ipAddress);
	
}
