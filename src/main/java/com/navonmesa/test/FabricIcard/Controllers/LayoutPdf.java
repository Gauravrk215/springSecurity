package com.realnet.FabricIcard.Controllers;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/token")
public class LayoutPdf {

	@PostMapping("/generate-pdf")
	public ResponseEntity<byte[]> generatePdf(@RequestBody String htmlContent) {
		try {
			// Use Flying Saucer to convert HTML to PDF
			byte[] pdfBytes = generatePdf1(htmlContent);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("inline", "output.pdf");

			return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public byte[] generatePdf1(String htmlContent) throws Exception {
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(htmlContent);
		renderer.layout();

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			renderer.createPDF(outputStream);
			return outputStream.toByteArray();
		}
	}



	@PostMapping("/generateHtml1/{id}")
	public String generateHtml1(@RequestBody List<Map<String, Object>> jsonData, @PathVariable Long id)
			throws JsonProcessingException {
		return generateHtml2(jsonData, id);
	}

	public String generateHtml2(List<Map<String, Object>> jsonData, Long id) throws JsonProcessingException {

//		LayoutBuilder_t layoutBuilder_t = layoutBuilder_Repository.findById(id).get();
//
//		LayoutBuilderLines_t layoutBuilderLines_t = layoutBuilderLines_Repository.findById(id).get();
//
//		String url = layoutBuilder_t.getUrl();
//		String model = layoutBuilderLines_t.getModel();
//
//		String serviceOrderId = "146";
//
//		JsonParser parser = new JsonParser();
//		JsonElement element = parser.parse(model);
//		JsonArray models = element.getAsJsonArray();
//		JsonObject getbodyobject = null;
//		List<Map<String, Object>> replacerule = null;
//		String operation = null;
//		String replaceWith = null;
//		String startstring = null;
//		String endstring = null;
//		String keyword = null;
//		String linestring = null;
//		String cellAddress = null;
//		String ModifyfileName = null;
//		String nodeName = null;
//
//		String a_uri = url.replace("?", String.valueOf(serviceOrderId));
//		System.out.println(a_uri);
//
//		Object body = GET(a_uri).getBody();
//		ObjectMapper objectMapper = new ObjectMapper();
//		String json = objectMapper.writeValueAsString(body);
//		JsonElement getbody = parser.parse(json);
//		getbodyobject = getbody.getAsJsonObject();
//
//		
//		
//		for (JsonElement mod : models) {
//			JsonObject object = mod.getAsJsonObject();
//
//			startstring = object.get("start_string").toString().replaceAll("\"", "");
//			endstring = object.get("end_string").toString().replaceAll("\"", "");
//			replaceWith = object.get("replace_with").toString().replaceAll("\"", "");
//			keyword = object.get("Keyword").toString().replaceAll("\"", "");
//			linestring = object.get("line_string").toString().replaceAll("\"", "");
//			operation = object.get("operation").toString().replaceAll("\"", "");
//			cellAddress = object.get("cellAddress").toString().replaceAll("\"", "");
//
//			nodeName = object.get("nodeName").toString().replaceAll("\"", "");
////					// nodeName = "listOfItems"; // Set the desired node name
////
//			JsonArray values = searchJsonKey(getbodyobject, replaceWith, nodeName);
//
//			for (JsonElement element1 : values) {
//				String individualValue = element1.getAsString();
//
//				replaceWith = individualValue;
//
//				System.out.println("Found value: " + individualValue);
//
//				if (operation.contains("replacement")) {
//					replacerule = replaceStringInJsonData(jsonData, replaceWith, keyword);
//
//				}
//			}
//
//		}
//
//		jsonData = replacerule;

		StringBuilder htmlBuilder = new StringBuilder();

		// Open the main card div
		htmlBuilder.append("<div class=\"card\" style=\"padding: 10px; background-color: white;\">");
		htmlBuilder.append(
				"<div class=\"card-body\" style=\"display: grid; grid-template-columns: repeat(20, 1fr); grid-template-rows: repeat(29, 1fr); gap: 5px;\">");

		for (Map<String, Object> item : jsonData) {

			// Check for Table type
			if ("Table".equals(item.get("type"))) {
				htmlBuilder.append("<div style=\"grid-column: ").append(item.get("x")).append(" / span ")
						.append(item.get("cols")).append("; grid-row: ").append(item.get("y")).append(" / span ")
						.append(item.get("rows")).append(";\">");

				// Open the card-title div for Table
				htmlBuilder.append("<div class=\"title-card card-title\" style=\"text-align: ")
						.append(item.get("alignment") != null ? item.get("alignment") : "left")
						.append("; line-height: 1; font-family: Metropolis; font-size: 100%; font-style: normal; font-weight: ")
						.append(item.get("bold") != null && (boolean) item.get("bold") ? "bold" : "normal")
						.append("; text-decoration: none; background-color: white; color: black;\">");

				// Add Table HTML code
				htmlBuilder.append("<div style=\"max-width:100%; overflow: auto;\">");
				htmlBuilder.append("<table class=\"table\" style=\"margin-top: 10px;\">");
				htmlBuilder.append("<thead>");
				htmlBuilder.append("<tr>");

				// Add table header based on the JSON structure
				List<Map<String, String>> values = (List<Map<String, String>>) item.get("values");
				
				
				for (Map<String, String> value : values) {
					htmlBuilder.append("<th>").append(value.get("label")).append("</th>");
				}

				htmlBuilder.append("</tr>");
				htmlBuilder.append("</thead>");
				htmlBuilder.append("<tbody>");
				htmlBuilder.append("<tr>");
				
				
		        // Add table body content based on the JSON structure
				for (Map<String, String> value : values) {
					htmlBuilder.append("<td>").append(value.get("value")).append("</td>");
				}
				
				
//				// Add the additional values twice
//				for (Map<String, String> additionalValue : values) {
//				    htmlBuilder.append("<td>").append(additionalValue.get("value")).append("</td>");
//				}

				htmlBuilder.append("</tr>");
				htmlBuilder.append("</tbody>");

				htmlBuilder.append("</table>");
				htmlBuilder.append("</div>");

				// Close the card-title and individual item div for Table
				htmlBuilder.append("</div></div>");
			}

			// Check for Image type
			else if ("Image".equals(item.get("type"))) {
				htmlBuilder.append("<div style=\"grid-column: ").append(item.get("x")).append(" / span ")
						.append(item.get("cols")).append("; grid-row: ").append(item.get("y")).append(" / span ")
						.append(item.get("rows")).append(";\">");

				// Open the card-title div for Image
				htmlBuilder.append("<div class=\"title-card card-title\" style=\"text-align: ")
						.append(item.get("alignment") != null ? item.get("alignment") : "left")
						.append("; line-height: 1; font-family: Metropolis; font-size: 100%; font-style: normal; font-weight: ")
						.append(item.get("bold") != null && (boolean) item.get("bold") ? "bold" : "normal")
						.append("; text-decoration: none; background-color: white; color: black;\">");

				// Add Image tag
				htmlBuilder.append("<img src=\"").append(item.get("imageURL"))
						.append("\" alt=\"Image\" style=\"width: ").append(item.get("imagewidth")).append("px;\">");

				// Close the card-title and individual item div for Image
				htmlBuilder.append("</div></div>");
			} else if ("Line".equals(item.get("type"))) {
				// Handle Line type
				htmlBuilder.append("<div style=\"grid-column: ").append(item.get("x")).append(" / span ")
						.append(item.get("cols")).append("; grid-row: ").append(item.get("y")).append(" / span ")
						.append(item.get("rows")).append(";\">");
				htmlBuilder.append("<hr></hr>");
				htmlBuilder.append("</div>");
			} else {
				// Handle other types
				// Open the individual item div
				htmlBuilder.append("<div style=\"grid-column: ").append(item.get("x")).append(" / span ")
						.append(item.get("cols")).append("; grid-row: ").append(item.get("y")).append(" / span ")
						.append(item.get("rows")).append(";\">");

				// Open the card-title div
				htmlBuilder.append("<div class=\"title-card card-title\" style=\"text-align: ")
						.append(item.get("alignment") != null ? item.get("alignment") : "left")
						.append("; line-height: 1; font-family: Metropolis; font-size: 100%; font-style: normal; font-weight: ")
						.append(item.get("bold") != null && (boolean) item.get("bold") ? "bold" : "normal")
						.append("; text-decoration: none; background-color: white; color: black;\">");

				// Append the fieldtext content
				htmlBuilder.append(item.get("fieldtext"));

				// Close the card-title and individual item div
				htmlBuilder.append("</div></div>");
			}
		}

		// Close the main card div
		htmlBuilder.append("</div></div>");

		return htmlBuilder.toString();
	}

	public ResponseEntity<Object> GET(String get) {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object> u = restTemplate.getForEntity(get, Object.class);

		return u;

	}

	private static JsonArray searchJsonKey(JsonObject jsonObject, String keyToSearch, String nodeName) {
		JsonArray resultArray = new JsonArray();

		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			String key = entry.getKey().replaceAll("\"", "");
			JsonElement value = entry.getValue();

			if (value.isJsonObject()) {
				// Recursively search for the key in the nested object
				JsonArray nestedValues = searchJsonKey(value.getAsJsonObject(), keyToSearch, nodeName);
				resultArray.addAll(nestedValues);
			} else if (nodeName == null || nodeName.isEmpty() || key.equalsIgnoreCase(nodeName)) {
				// If nodeName is null or matches the current key, search for the keyToSearch
				if (value.isJsonArray()) {
					// Handle arrays by iterating through each element
					JsonArray jsonArray = value.getAsJsonArray();
					for (JsonElement arrayElement : jsonArray) {
						if (arrayElement.isJsonObject()) {
							JsonObject itemObject = arrayElement.getAsJsonObject();
							if (itemObject.has(keyToSearch)) {
								resultArray.add(itemObject.get(keyToSearch));
							}
						}
					}
				} else if (value.isJsonPrimitive() && key.equals(keyToSearch)) {
					// If nodeName is null and the current key matches keyToSearch, add the value to
					// the result
					resultArray.add(value);
				}
			}
		}

		return resultArray;
	}



	public static List<Map<String, Object>> replaceStringInJsonData(List<Map<String, Object>> jsonData, String keyword,
			String replaceWith) {
		for (Map<String, Object> entry : jsonData) {
			for (Map.Entry<String, Object> keyValue : entry.entrySet()) {
				if (keyValue.getValue() instanceof String) {
					String value = (String) keyValue.getValue();
					if (value.contains(replaceWith)) {
						entry.replace(keyValue.getKey(), value.replace(replaceWith, keyword));
					}
				}
			}
		}
		return jsonData;
	}

}
