package com.realnet.startCoder.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.startCoder.Services.StarCoderService;

@RestController
@RequestMapping("/codegen")
public class StarCoderController {

	@Autowired
	private StarCoderService starCoderService;

	@PostMapping
	public String generateCode(@RequestBody String prompt) {
		return starCoderService.generateCode(prompt);
	}
}
