package com.satendra.wssecurity.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/emp1")
	public Collection<String> getEmployee1(){
		return List.of("emp1","emp1","emp1","emp1");
	}

	@GetMapping("/emp2")
	public Collection<String> getEmployee2(){
		return List.of("emp2","emp2","emp2","emp2");
	}

}
