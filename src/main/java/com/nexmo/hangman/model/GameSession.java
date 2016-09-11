package com.nexmo.hangman.model;

import com.nexmo.hangman.enums.GameState;
import com.nexmo.hangman.model.User;

public class GameSession extends AbstractGameSession {

	/** Appease the Gods of serialization*/
	private static final long serialVersionUID = 6155929039313988950L;
	private String currentWord;
	private String ipAddress;
	
	public GameSession(String ipAddress, User user, int maxNumberOfAttempts){
		super();
		this.setIpAddress(ipAddress);
		this.setUser(user);
		this.setNumberOfRemainingAttempts(maxNumberOfAttempts);
	}
	
	public void generateDummyWord(){
		
	}
	
	public void updateDummyWord(){
		
	}

	public void reduceRemainingAttempts(int maxNumberOfAttempts){
		this.numberOfRemainingAttempts--;
		if(this.numberOfRemainingAttempts == 0){
			this.setCurrentGameState(GameState.LOST_STATE);
		}
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the currentWord
	 */
	public String getCurrentWord() {
		return currentWord;
	}

	/**
	 * @param currentWord the currentWord to set
	 */
	public void setCurrentWord(String currentWord) {
		this.currentWord = currentWord;
	}



}
