package com.nexmo.hangman.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.nexmo.hangman.model.InGameSession;
import com.nexmo.hangman.service.GameService;

@RestController
@RequestMapping(value="/game")
public class GameController {

	@Autowired
	private GameService gameService;

	@RequestMapping(value="/retrieveExistingGame", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> retrieveExistingGame(HttpServletRequest request){
		String ipAddress = request.getRemoteAddr();
		if(ipAddress != null){
			InGameSession inGame = gameService.retrieveExistingGameSession(ipAddress);
			if(inGame != null) return new ResponseEntity<String>(new Gson().toJson(inGame), HttpStatus.OK);
		}
		return new ResponseEntity<>("Unable to retrieve a game", HttpStatus.FORBIDDEN);
	}

	@RequestMapping(value="/beginGame", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> beginGame(HttpServletRequest request, @RequestParam("username") final String username){
		
		if(StringUtils.hasText(username)){
			String ipAddress = request.getRemoteAddr();
			if(ipAddress != null){
				//boolean newGame = gameService.checkGameSessionExists(ipAddress);
				InGameSession inGame = gameService.createGameSession(ipAddress, username);
				//JsonObject rtnObject = new JsonObject();
				//rtnObject.addProperty("user", username.toUpperCase());
				//rtnObject.add("inGameSession", new Gson().toJsonTree(inGame));
				if(inGame != null) return new ResponseEntity<String>(new Gson().toJson(inGame), HttpStatus.OK);
				return new ResponseEntity<>("Unable to retrieve a game", HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>("Unable to retrieve a game", HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>("Please Enter a username", HttpStatus.FORBIDDEN);
	}
	
	@RequestMapping(value="/submitAttempt", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> submitAttempt(HttpServletRequest request, @RequestParam("letter") final String letter){
		try{
			if(StringUtils.hasText(letter)){
				String ipAddress = request.getRemoteAddr();
				if(ipAddress != null){
					InGameSession inGame = gameService.makeAttempt(ipAddress, letter);
					if(inGame != null) return new ResponseEntity<String>(new Gson().toJson(inGame), HttpStatus.OK);
					return new ResponseEntity<>("Unable to retrieve a game", HttpStatus.FORBIDDEN);
				}
				throw new Exception();
			}
			throw new Exception();
		}catch (Exception ex){
			return new ResponseEntity<>("Unable to make a game", HttpStatus.FORBIDDEN);
		}

		
		//return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		
	}
	
	public class Something implements Serializable{
		/** Appease the Gods of serialization*/
		private static final long serialVersionUID = 2431276675850204592L;
		private int remainingAttempts;
		private transient boolean worked;
		
		public int getRemainingAttempts() {
			return remainingAttempts;
		}
		public void setRemainingAttempts(int remainingAttempts) {
			this.remainingAttempts = remainingAttempts;
		}
		public boolean isWorked() {
			return worked;
		}
		public void setWorked(boolean worked) {
			this.worked = worked;
		}
	}
}
