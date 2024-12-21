package com.realnet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.realnet.Builders.Services.BuilderService;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

	@Autowired
	private BuilderService builderService;

	@Override
	public void run(String... args) throws Exception {

		System.out.println("call command line runner...");
		if (args.length == 0) {
			System.err.println("Usage: java -jar your-app.jar proj_id profile_id addString \n");
			builderService.callotherService();

			System.out.println("call other service");

			return;
		}
		if (args.length == 1) {
			System.err.println("Usage: java -jar your-app.jar proj_id profile_id addString \n");
			builderService.callotherService();

			System.out.println("call other service");

			return;
		}

	}

	public ResponseEntity<Object> GET(String get) {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object> u = restTemplate.getForEntity(get, Object.class);

		return u;

	}

}
