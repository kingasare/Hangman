package com.nexmo.hangman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.nexmo.hangman.manager.GameManager;
import com.nexmo.hangman.service.GameService;
import com.nexmo.hangman.service.impl.GameServiceImpl;

@Configuration
@PropertySource(value="classpath:/config/config.properties", ignoreResourceNotFound = true)
@ComponentScan(basePackages="com.nexmo.hangman")
public class AppConfiguration {

	@Bean
	public GameService gameService(){
		return new GameServiceImpl();
	}

	@Bean
	public GameManager gameManager(){
		return new GameManager();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
