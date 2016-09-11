package com.nexmo.hangman.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import com.nexmo.hangman.enums.GameState;
import com.nexmo.hangman.model.GameSession;
import com.nexmo.hangman.model.User;


public class GameManager implements Serializable {
	/** Appease the Gods of serialization */
	private static final long serialVersionUID = 6086444376302321730L;
	private static final String TEMP_DIR_LOCATION = "hangmanGame";
	private static final String TEMP_FILE_FILENAME = "gameSessions";
	private static final String TEMP_FILE_EXT = "ser"; 
	private static final int MAX_NUMBER_OF_ATTEMPTS = 7;
	private ArrayList<String> wordList = new ArrayList<String>();
	private HashMap<String, GameSession> gameSessions = new HashMap<String, GameSession>();

	@Value("classpath:wordList/wordList.txt")
	private Resource wordListResource;

	@Value("${hangman.gameSessionLocation}")
	private String gameSessionLocation;

	public GameManager(){
	}

	@PostConstruct
	public void init(){
		retrieveWordList();
		retrieveGameSessions();
	}

	public ArrayList<String> getWordList() {
		return wordList;
	}

	public void setWordList(ArrayList<String> wordList) {
		this.wordList = wordList;
	}

	private void retrieveWordList(){
		if(wordListResource != null && wordListResource.exists() && wordListResource.isReadable()){
			try (
					InputStream in = wordListResource.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				){

				String word;
				while((word = reader.readLine()) != null){
					this.wordList.add(word);
				}
			} catch (IOException e) {
				e.printStackTrace();
				//since weren't able to add words from the wordList File
				// just add two words to the list so that the game can still be played
				this.wordList.add("maximizes");
				this.wordList.add("piggyback");
			}
		}
	}

	public boolean containsGameSession(final String ipAddress){
		if(StringUtils.hasText(ipAddress)){
			return this.gameSessions.containsKey(ipAddress);
		}
		return false;
	}

	public GameSession getGameSession(final String ipAddress){
		return this.gameSessions.get(ipAddress);
	}

	public void addGameSession(String ipAddress, GameSession gameSession){
		this.gameSessions.put(ipAddress, gameSession);
	}

	public GameSession createGameSession(String ipAddress, String userName){
		GameSession gameSession = null;

		if(StringUtils.hasText(ipAddress) && StringUtils.hasText(userName)){
			final int wordID = getRandomWordID();
			User user = new User(userName);
			gameSession = new GameSession(ipAddress, user, MAX_NUMBER_OF_ATTEMPTS);
			gameSession.setWordID(wordID);
			gameSession.setCurrentWord(getWord(wordID));
			gameSession.setDummyWord(generateDummyWord(wordID));
			gameSession.setCurrentGameState(GameState.PLAYING_STATE);	
			
			this.gameSessions.put(ipAddress, gameSession);
		}

		return gameSession;
	}

	public String getWord(int wordID){
		String word = null;
		if(wordID >= 0 && wordID <= this.wordList.size()){
			word = this.wordList.get(wordID);
		}
		return word;
	}

	private String generateDummyWord(int wordID){
		String dummyWord = null;
		if(wordID >= 0 && wordID <= this.wordList.size()){
			dummyWord = this.wordList.get(wordID).replaceAll(".", "_");
		}
		return dummyWord;
	}

	public boolean makeAttempt(String ipAddress, String letter) throws Exception{
		if(StringUtils.hasText(ipAddress) && StringUtils.hasText(letter) && getGameSession(ipAddress) != null){
			GameSession gameSession = getGameSession(ipAddress);
			String currentWord = gameSession.getCurrentWord();
			String dummyWord = gameSession.getDummyWord();

			if(currentWord.contains(letter) && !(dummyWord.contains(letter))){
				String newDummyWord = generateNewDummyWord(currentWord, dummyWord, letter);
				this.gameSessions.get(ipAddress).setDummyWord(newDummyWord);
				this.gameSessions.get(ipAddress).getSucessfulLetters().add(letter);
				if(currentWord.equalsIgnoreCase(newDummyWord)){
					this.gameSessions.get(ipAddress).setCurrentGameState(GameState.WIN_STATE);
				}
				return true;
			}else{
				this.gameSessions.get(ipAddress).reduceRemainingAttempts(MAX_NUMBER_OF_ATTEMPTS);
				return false;
			}
		}
		throw new Exception();
	}

	private String generateNewDummyWord(String currentWord, String dummyWord, String letter) throws Exception{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < currentWord.length(); i++){
			if(currentWord.charAt(i) == letter.charAt(0)){
				sb.append(letter);
			}else{
				sb.append(dummyWord.charAt(i));
			}
		}

		return sb.toString();
	}

	private int getRandomWordID(){
		if(!this.wordList.isEmpty()){
			Random ran = new Random();
			return ran.nextInt(this.wordList.size());
		}
		return -1;
	}

	/**
	 * @return the wordListResource
	 */
	public Resource getWordListResource() {
		return wordListResource;
	}

	/**
	 * @param wordListResource the wordListResource to set
	 */
	public void setWordListResource(Resource wordListResource) {
		this.wordListResource = wordListResource;
	}

	private void retrieveGameSessions(){
		String tempDirName = this.gameSessionLocation;
		if(StringUtils.hasText(tempDirName)){
			File tempSessionFile = new File(tempDirName + "/" + TEMP_DIR_LOCATION + "/" + TEMP_FILE_FILENAME + "." + TEMP_FILE_EXT);
			if(tempSessionFile.exists() && tempSessionFile.isFile()){
				try (
						FileInputStream fis = new FileInputStream(tempSessionFile);
						ObjectInputStream ois = new ObjectInputStream(fis);
						){
					HashMap<String, GameSession> gsf = (HashMap<String, GameSession>) ois.readObject();
					
					if(gsf != null) this.gameSessions = gsf;
				}catch(IOException | ClassNotFoundException e){
					e.printStackTrace();
				} 
			}
		}
	}

	@PreDestroy
	private void saveGameSessions() throws IOException{
		if(this.gameSessions != null){
			String tempDirName = this.gameSessionLocation;
			
			File tempDir = new File(tempDirName);
			if(tempDir.isDirectory()){
				File tempSessionFile = new File(tempDirName + "/" + TEMP_DIR_LOCATION + "/" + TEMP_FILE_FILENAME + "." + TEMP_FILE_EXT);
				if(!(tempSessionFile.exists() && tempSessionFile.isFile())){
					File parentTempSessionFile = new File(tempSessionFile.getParentFile().getAbsolutePath());
					parentTempSessionFile.mkdirs();
					tempSessionFile.createNewFile();
				}

				try (
						FileOutputStream fos = new FileOutputStream(tempSessionFile, false);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
					) {
						oos.writeObject(this.gameSessions);
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
				}
			}
		}
	}


}
