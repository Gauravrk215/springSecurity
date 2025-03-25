package com.realnet.CredentialDatabase.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.realnet.utils.Port_Constant;

@Service
public class SurevaultService {

	@Value("${spring.datasource.url}")
	public String url;

	@Value("${spring.datasource.username}")
	public String username;

	@Value("${spring.datasource.password}")
	public String password;

	public String getSurevaultCredentials(String key) throws JsonProcessingException {

//		String SurevaultDeploymentType = Port_Constant.SUREVAULT_DEPLOYMENT_TYPE;
//		System.out.println(SurevaultDeploymentType);
//		// Obtain the token
//		String token = callconnector("surevault").toString();
//
//		String surevaultApiUrl =Port_Constant.SURE_VAULT_DOMAIN + "/getcredentials/" + SurevaultDeploymentType;
//
//		ResponseEntity<Object> get = GET(surevaultApiUrl, token);
//
//		Object responseBody = get.getBody();
//
//		JsonNode jsonNode = new ObjectMapper().convertValue(responseBody, JsonNode.class);
//		String value = jsonNode.get(key).asText();

		String value = "";
		switch (key) {
		case "databaseUrl":

			value = url;
			break;

		case "databaseuserName":

			value = username;
			break;

		case "databasePassword":

			value = password;
			break;

		default:
			break;
		}

		return value;

	}

	public String callconnector(String name) throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		String url = Port_Constant.SURE_VAULT_DOMAIN + "/token/Sure_Connectbyname/" + name;
		System.out.println(Port_Constant.SURE_VAULT_DOMAIN);

		ResponseEntity<Object> u = restTemplate.getForEntity(url, Object.class);
		Object object = u.getBody();

		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(object);

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(str);

		JsonObject obj = element.getAsJsonObject();
		JsonElement token = obj.get("access_token");
		System.out.println("token is == " + token);
		return token.getAsString();
	}

	public ResponseEntity<Object> GET(String get, String token) {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl = get;
		String token1 = "Bearer " + token;
		HttpHeaders headers = getHeaders();
		headers.set("Authorization", token1);
		HttpEntity<Object> request = new HttpEntity<Object>(headers);
		ResponseEntity<Object> u = restTemplate.exchange(resourceUrl, HttpMethod.GET, request, Object.class);

		int statusCodeValue = u.getStatusCodeValue();
		System.out.println(statusCodeValue);

		return u;

	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

}
