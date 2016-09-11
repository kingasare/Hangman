package com.nexmo.hangman.model;

import java.io.Serializable;

public class User implements Serializable {
	/** Appease the Gods of serialization*/
	private static final long serialVersionUID = -4608372412882454017L;

	private String userName;

	public User(){
		
	}

	public User(final String userName){
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
