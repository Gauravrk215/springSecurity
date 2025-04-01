//package com.realnet.FabricIcard.Controllers;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/token")
//public class TestCode {
//	 @GetMapping("/dynamicValues")
//	    public List<Map<String, String>> getDynamicValues() {
//	        try {
//	            // Make HTTP request to the first API
//	            String apiUrl = "http://3.109.61.114:30161/token/Billing/ServiceOrder/ServiceOrder/146";
//	            String response = sendHttpRequest(apiUrl);
//
//	            // Parse the response and extract the listOfItems
//	            return createValuesFromList(response);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return new ArrayList<>(); // Return an empty list in case of an error
//	        }
//	    }
//
//	    private String sendHttpRequest(String apiUrl) throws Exception {
//	        URL url = new URL(apiUrl);
//	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//	        connection.setRequestMethod("GET");
//
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//	        StringBuilder response = new StringBuilder();
//
//	        String line;
//	        while ((line = reader.readLine()) != null) {
//	            response.append(line);
//	        }
//
//	        reader.close();
//	        connection.disconnect();
//
//	        return response.toString();
//	    }
//
//	    private List<Map<String, String>> createValuesFromList(String jsonResponse) {
//	        List<Map<String, String>> values = new ArrayList<>();
//
//	        try {
//	            // Parse the JSON response using Jackson ObjectMapper
//	            ObjectMapper objectMapper = new ObjectMapper();
//	            JsonNode rootNode = objectMapper.readTree(jsonResponse);
//
//	            // Extract the listOfItems node
//	            JsonNode listOfItemsNode = rootNode.path("listOfItems");
//
//	            // Iterate through each item in the listOfItems
//	            Iterator<JsonNode> itemsIterator = listOfItemsNode.elements();
//	            while (itemsIterator.hasNext()) {
//	                JsonNode itemNode = itemsIterator.next();
//
//	                // Extract relevant information from the item
//	                String itemCode = itemNode.path("itemCode").asText();
//	                String unitPrice = itemNode.path("unitPrice").asText();
//	                String lineTotal = itemNode.path("lineTotal").asText();
//
//	                // Create the corresponding values
//	                Map<String, String> itemValues = Map.of(
//	                        "label", "Item and Description",
//	                        "value", itemCode
//	                );
//	                Map<String, String> qtyValues = Map.of(
//	                        "label", "Qty",
//	                        "value", "1"
//	                );
//	                Map<String, String> rateValues = Map.of(
//	                        "label", "Rate",
//	                        "value", unitPrice
//	                );
//	                Map<String, String> amountValues = Map.of(
//	                        "label", "Amount",
//	                        "value", lineTotal
//	                );
//
//	                // Add the values to the list
//	                values.add(itemValues);
//	                values.add(qtyValues);
//	                values.add(rateValues);
//	                values.add(amountValues);
//	            }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//
//	        return values;
//	    }
//}
