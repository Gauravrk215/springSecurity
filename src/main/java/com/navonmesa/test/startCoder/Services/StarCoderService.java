package com.realnet.startCoder.Services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StarCoderService {

	private static final String MODEL_API_URL = "http://localhost:8000";

	public String generateCode(String prompt) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		String requestBody = String.format("{\"inputs\": \"%s\"}", prompt);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(MODEL_API_URL, HttpMethod.POST, request, String.class);

		return response.getBody();
	}
}
