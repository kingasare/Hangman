package com.nexmo.hangman.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

	@RequestMapping(value="/")
	public ModelAndView getHomepage(HttpServletResponse response) throws IOException{
		return new ModelAndView("home");
	}
}
