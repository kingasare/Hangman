package com.nexmo.hangman.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nexmo.hangman.enums.GameState;

public abstract class AbstractGameSession implements Serializable{
	/** Appease the Gods of serialization*/
	private static final long serialVersionUID = -3676066601208754251L;
	protected int numberOfRemainingAttempts;
	private int wordID;
	private String dummyWord;
	private GameState currentGameState;
	private User user;
	private List<String> successfulLetters = new ArrayList<String>();
	private List<String> unSuccessfulLetters = new ArrayList<String>();

	public int getNumberOfRemainingAttempts() {
		return numberOfRemainingAttempts;
	}

	public void setNumberOfRemainingAttempts(int numberOfRemainingAttempts) {
		this.numberOfRemainingAttempts = numberOfRemainingAttempts;
	}

	public String getDummyWord() {
		return dummyWord;
	}

	public void setDummyWord(String dummyWord) {
		this.dummyWord = dummyWord;
	}

	public GameState getCurrentGameState() {
		return currentGameState;
	}

	public void setCurrentGameState(GameState currentGameState) {
		this.currentGameState = currentGameState;
	}
	/**
	 * @return the wordID
	 */
	public int getWordID() {
		return wordID;
	}
	/**
	 * @param wordID the wordID to set
	 */
	public void setWordID(int wordID) {
		this.wordID = wordID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getSuccessfulLetters() {
		return successfulLetters;
	}

	public void setSuccessfulLetters(List<String> successfulLetters) {
		this.successfulLetters = successfulLetters;
	}

	/**
	 * @return the unSuccessfulLetters
	 */
	public List<String> getUnSuccessfulLetters() {
		return unSuccessfulLetters;
	}

	/**
	 * @param unSuccessfulLetters - the unsuccessful letters to set
	 */
	public void setUnSuccessfulLetters(List<String> unSuccessfulLetters) {
		this.unSuccessfulLetters = unSuccessfulLetters;
	}
}
