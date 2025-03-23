package com.realnet.api_registery.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.realnet.api_registery.Entity.Token_registery;
import com.realnet.api_registery.Services.GeneratetokenService;

@RestController
@RequestMapping(value = "/apiregistery")
public class GeneratetokenController {

	@Autowired
	private GeneratetokenService generatetokenService;

	@PostMapping("/generateToken")
	public String generatetoken(@RequestParam String token_name) throws JsonProcessingException {

		String token = generatetokenService.generatetoken(token_name);

		return token;

	}

	@GetMapping("/getall")
	public List<Token_registery> getall() {

		List<Token_registery> getall = generatetokenService.getall();

		return getall;

	}

	@DeleteMapping("/delete")
	public void delete(@PathVariable Integer id) {

		generatetokenService.delete(id);

	}

}
