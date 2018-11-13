package com.szakdolg.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionGeneral {
	
	public String exception(Exception ex, Model model) {
		model.addAttribute("exception", ex);
		System.out.println(ex.getMessage());
		return "exceptionHandler";
	}

}
