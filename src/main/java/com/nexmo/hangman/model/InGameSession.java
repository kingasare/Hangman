package com.nexmo.hangman.model;

import java.io.Serializable;

public class InGameSession extends AbstractGameSession implements Serializable {

	/** Appease the Gods of serialization*/
	private static final long serialVersionUID = -8560306425013887094L;
	private static final String HANGMAN_IMAGE_URL_PREFIX = "resources/core/img/";
	private boolean successfulAttempt;
	@SuppressWarnings("unused")
	private String hangmanImgPath;

	public InGameSession(){
		super();
	}

	/**
	 * @return the success
	 */
	public boolean isSuccessfulAttempt() {
		return this.successfulAttempt;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccessfulAttempt(boolean successfulAttempt) {
		this.successfulAttempt = successfulAttempt;
	}

	@Override
	public void setNumberOfRemainingAttempts(int numberOfRemainingAttempts){
		super.setNumberOfRemainingAttempts(numberOfRemainingAttempts);
		this.hangmanImgPath = HANGMAN_IMAGE_URL_PREFIX + numberOfRemainingAttempts + ".png";
	}
}
