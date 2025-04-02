package com.realnet.FabricIcardLines.Controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realnet.FabricIcardLines.Entity.FabricIcardLines;
import com.realnet.FabricIcardLines.Services.FabricIcardLinesService;

@CrossOrigin("*")
@RequestMapping(value = "/FabricIcardLines")
@RestController
public class JsonKeysController {

	@Autowired
	private FabricIcardLinesService fabricIcardLinesService;

	@PostMapping("/getJsonKeys")
	public ResponseEntity<List<String>> getJsonKeys(@RequestBody String json) {
		try {
			// Parse JSON string into JsonNode
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(json);

			// Get all keys from JsonNode
			List<String> keys = getAllKeys(jsonNode);

			return ResponseEntity.ok(keys);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private List<String> getAllKeys(JsonNode jsonNode) {
		List<String> keys = new ArrayList<>();
		Iterator<String> fieldNames = jsonNode.fieldNames();
		while (fieldNames.hasNext()) {
			keys.add(fieldNames.next());
		}
		return keys;
	}

//	get key value from FabricIcardLinesService
	@PostMapping("/FabricIcardLines/getKeyValuePairs/{id}")
	public ResponseEntity<List<Map<String, String>>> getKeyValuePairs(@PathVariable String id) {

		FabricIcardLines lines = fabricIcardLinesService.getbyheaderid(id);
		if (lines == null) {
			String message = "No lines found for ID: " + id;
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonList(Collections.singletonMap("message", message)));
		}

		String jsonData = lines.getLayoutModel();

		try {
			// Parse JSON string into JsonNode
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(jsonData);

			// Extract key-value pairs based on type
			List<Map<String, String>> keyValuePairs = extractKeyValuePairs(rootNode);

			return ResponseEntity.ok(keyValuePairs);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private List<Map<String, String>> extractKeyValuePairs(JsonNode rootNode) {
		List<Map<String, String>> keyValuePairs = new ArrayList<>();
		JsonNode objectsNode = rootNode.get("objects");
		if (objectsNode == null || !objectsNode.isArray()) {
			return keyValuePairs; // Return empty list if objects array is missing or not an array
		}

		for (JsonNode node : objectsNode) {
			Map<String, String> pair = new HashMap<>();
			String type = node.path("type").asText();
			if (type.equals("i-text")) {
				JsonNode textNode = node.path("text");
				if (!textNode.isMissingNode()) {
					pair.put(type, textNode.asText());
				}
			} else if (type.equals("image")) {
				JsonNode srcNode = node.path("src");
				if (!srcNode.isMissingNode()) {
					pair.put(type, node.get("src").asText());

				}
			}
			if (!pair.isEmpty()) {

				keyValuePairs.add(pair);
			}
		}
		return keyValuePairs;
	}

}
