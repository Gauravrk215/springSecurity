package com.realnet.OpenAi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.OpenAi.Models.openAi;
import com.realnet.OpenAi.Services.OpenAiServices;

@RestController
@RequestMapping("/token/openAi")
public class OpenAiController {

	@Autowired
	private OpenAiServices openAiServices;

	@PostMapping
	public String chat(@RequestBody openAi request) {
		String prompt = request.getPrompt();

		System.out.println("open api start..");
		return openAiServices.getChatGPTResponse(prompt);
	}

	public String fallbackResponse(Exception e) {
		return "Rate limit exceeded. Please try again later.";
	}
}
