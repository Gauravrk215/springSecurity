package com.realnet.Rpt_builder2_lines.Controllers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.realnet.Rpt_builder2_lines.Entity.Rpt_builder2_lines_t;
import com.realnet.Rpt_builder2_lines.Repository.Rpt_builder2_lines_Repository;
import com.realnet.Rpt_builder2_lines.Services.Rpt_builder2_lines_Service;

@RequestMapping(value = "/Rpt_builder2_lines")
@RestController
public class Rpt_builder2_lines_Controller {

	@Autowired
	private Rpt_builder2_lines_Service Service;
	
	@Autowired
	private Rpt_builder2_lines_Repository line_repo;

	@PostMapping("/Rpt_builder2_lines")
	public Rpt_builder2_lines_t Savedata(@RequestBody Rpt_builder2_lines_t data) {
		Rpt_builder2_lines_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/Rpt_builder2_lines")
	public List<Rpt_builder2_lines_t> getdetails() {
		List<Rpt_builder2_lines_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Rpt_builder2_lines/{id}")
	public Rpt_builder2_lines_t getdetailsbyId(@PathVariable Long id) {
		Rpt_builder2_lines_t get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/Rpt_builder2_lines/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/Rpt_builder2_lines/{id}")
	public Rpt_builder2_lines_t update(@RequestBody Rpt_builder2_lines_t data, @PathVariable Long id) {
		Rpt_builder2_lines_t update = Service.update(data, id);
		return update;
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody Rpt_builder2_lines_t outgoing_data) {
		Rpt_builder2_lines_t line = line_repo.findById(id).get();
		line.setHeader_id(outgoing_data.getHeader_id());
		line.setModel(outgoing_data.getModel());
		Rpt_builder2_lines_t save = line_repo.save(line);
		return new ResponseEntity<>(save, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/geturlkeybyurl")
	public Set<String> getKeysById(@RequestParam String url) {
	    ResponseEntity<Object> response = GET(url);
	    System.out.println("Response status code: " + response.getStatusCodeValue());
	    // Check the class of the response body
	    if (response.getBody() != null) {
	        System.out.println("Response body class: " + response.getBody().getClass());
	    }
	    // Check if the response body is an ArrayList
	    if (response.getBody() instanceof ArrayList) {
	        ArrayList<Object> responseBody = (ArrayList<Object>) response.getBody();
	        // Assuming you want to extract keys from elements inside the ArrayList,
	        // you might need to iterate through the list and extract keys from each element.
	        Set<String> keys = new HashSet<>();
	        for (Object element : responseBody) {
	            if (element instanceof LinkedHashMap) {
	                LinkedHashMap<String, Object> elementMap = (LinkedHashMap<String, Object>) element;
	                keys.addAll(elementMap.keySet());
	            }
	        }
	        return keys;
	    } else {
	        return null;
	    }
	}

	public ResponseEntity<Object> GET(String get) {
		org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
		ResponseEntity<Object> u = restTemplate.getForEntity(get, Object.class);
		return u;
	}
	
	@GetMapping("/fetch_data_url")
    public ResponseEntity<Object> fetchURL(@RequestParam String url) {
        try {
            // Create an instance of RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Send an HTTP GET request to the provided URL and retrieve the response body as a String
            String responseBody = restTemplate.getForObject(url, String.class);

            // Create a Map to hold the URL and body data
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("url", url);
            responseMap.put("body", responseBody);

            // Return the response map as JSON
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            // Handle exceptions, such as invalid URLs or network errors
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to fetch URL: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }
    }
	
	
	
}