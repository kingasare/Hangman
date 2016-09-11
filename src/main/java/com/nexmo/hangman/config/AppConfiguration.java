package com.nexmo.hangman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.nexmo.hangman.manager.GameManager;
import com.nexmo.hangman.service.GameService;
import com.nexmo.hangman.service.impl.GameServiceImpl;

@Configuration
@PropertySource("classpath:config/config.properties")
@ComponentScan(basePackages="com.nexmo.hangman")
public class AppConfiguration {

	@Bean
	public GameService gameService(){
		return new GameServiceImpl();
	}
	
	@Bean
	public GameManager gameManager(){
		//GameManager gameManager = new GameManager();
		// gameManager.setWordListResource(resourceLoader.getResource("classpath:classpathdata.txt"););
		return new GameManager();
	}
}
