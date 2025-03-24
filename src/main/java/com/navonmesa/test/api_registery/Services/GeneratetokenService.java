package com.realnet.api_registery.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.realnet.api_registery.Entity.Token_registery;
import com.realnet.api_registery.Repository.Token_registery_Repository;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;

@Service
public class GeneratetokenService {

	@Autowired
	private AppUserServiceImpl userServiceImpl;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private Token_registery_Repository token_registery_Repository;

	public String generatetoken(String toekn_name) throws JsonProcessingException {

		AppUser loggedInUser = userServiceImpl.getLoggedInUser();

		String username = loggedInUser.getUsername();

		String userPassw = loggedInUser.getUserPassw();

		StringBuilder builder = new StringBuilder();

		builder.append(
				"{\n" + "    \"email\" :\"" + username + "\",\n" + "    \"password\" : \"" + userPassw + "\"\n" + "}");

		String url = "http://localhost:9292";
		String repo = "/token/session";

		// ADD DATA IN GITEA

		System.out.println(builder.toString());

		String job_url = url + repo;

		RestTemplate restTemplate = new RestTemplate();

		String resourceUrl = job_url;
		HttpHeaders headers = getHeaders();
		HttpEntity<Object> request = new HttpEntity<Object>(builder.toString(), headers);

		ResponseEntity<String> res = restTemplate.postForEntity(resourceUrl, request, String.class);
		Object object = res.getBody();

		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(object);

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(str);

		JsonObject obj = element.getAsJsonObject();
		JsonObject item = obj.get("item").getAsJsonObject();

		String token = item.get("token").getAsString();

		Token_registery token_registery = new Token_registery();
		token_registery.setToken(token);
		token_registery.setToken_name(toekn_name);
		token_registery.setCreatedBy(loggedInUser.getUserId());

		System.out.println("token is == " + token);

		return token;

	}

	public List<Token_registery> getall() {
		AppUser loggedInUser = userServiceImpl.getLoggedInUser();

		Long userId = loggedInUser.getUserId();

		List<Token_registery> list = token_registery_Repository.getallbycreatedby(userId);

		return list;

	}

	public void delete(Integer id) {

		token_registery_Repository.deleteById(id);

	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	public ResponseEntity<Object> GET(String get) {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object> u = restTemplate.getForEntity(get, Object.class);

		return u;

	}

}
