package com.realnet.FabricIcard.Services;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class LayoutPdfService {


	public String generateForm(@RequestBody Map<String, Object> jsonData) {
		// Convert JSON to HTML
		String htmlForm = convertJsonToHtml(jsonData);
		return htmlForm;
	}

//	public void convertToPdf(@RequestBody String htmlContent) throws IOException {
//		// Render HTML content using Flying Saucer
//		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//			ITextRenderer renderer = new ITextRenderer();
//			renderer.setDocumentFromString(htmlContent);
//			renderer.layout();
//			renderer.createPDF(outputStream);
//			
//			
//			 // Specify the directory and file name where you want to save the PDF
//	        String pdfFilePath = "/data/example.pdf";
//
//	        String path="/data";
//	        
//	        Path directory = Paths.get(path);
//	        if (!Files.exists(directory)) {
//	            try {
//	                Files.createDirectories(directory);
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	                // Handle directory creation failure appropriately
//	            }
//	        }
//	        
//	      
//	        // Save the PDF to the specified file
//	        try (FileOutputStream fileOutputStream = new FileOutputStream(pdfFilePath)) {
//	            outputStream.writeTo(fileOutputStream);
//	        }
//			
//
//			// Set the response headers
////			response.setContentType("application/pdf");
////			response.setContentLength(outputStream.size());
////			response.setHeader("Content-Disposition", "inline; filename=example.pdf");
////
////			// Write the PDF to the response output stream
////			response.getOutputStream().write(outputStream.toByteArray());
////			response.getOutputStream().flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//			// Handle exceptions appropriately
//		}
//	}
	
	
	
	public void convertToPdf(@RequestBody String htmlContent) throws IOException {
		
		htmlContent="<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\">\r\n"
				+ "<head>\r\n"
				+ " <meta charset=\"UTF-8\"></meta>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
				+ "    <title>FormName</title>\r\n"
				+ "    <style>\r\n"
				+ "        body {\r\n"
				+ "            font-family: Arial, sans-serif;\r\n"
				+ "            margin: 20px;\r\n"
				+ "        }\r\n"
				+ "        \r\n"
				+ "        .dashboard {\r\n"
				+ "            display: grid;\r\n"
				+ "            grid-template-columns: repeat(24, 1fr);\r\n"
				+ "            grid-template-rows: repeat(2, 1fr);\r\n"
				+ "            gap: 10px;\r\n"
				+ "        }\r\n"
				+ "        \r\n"
				+ "        .dashboard-item {\r\n"
				+ "            grid-column: span 6;\r\n"
				+ "            grid-row: span 2;\r\n"
				+ "            border: 1px solid #ccc;\r\n"
				+ "            padding: 10px;\r\n"
				+ "            box-sizing: border-box;\r\n"
				+ "        }\r\n"
				+ "        \r\n"
				+ "        .text-center {\r\n"
				+ "            text-align: center;\r\n"
				+ "        }\r\n"
				+ "        \r\n"
				+ "        .bold {\r\n"
				+ "            font-weight: bold;\r\n"
				+ "        }\r\n"
				+ "        \r\n"
				+ "        .italic {\r\n"
				+ "            font-style: italic;\r\n"
				+ "        }\r\n"
				+ "        \r\n"
				+ "        .underline {\r\n"
				+ "            text-decoration: underline;\r\n"
				+ "        }\r\n"
				+ "        \r\n"
				+ "        .larger {\r\n"
				+ "            font-size: larger;\r\n"
				+ "        }\r\n"
				+ "    </style>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "\r\n"
				+ "<div class=\"dashboard\">\r\n"
				+ "    <div class=\"dashboard-item text-center bold italic underline larger\" style=\"grid-column: span 3; grid-row: span 2;\">\r\n"
				+ "        Title\r\n"
				+ "    </div>\r\n"
				+ "    <div class=\"dashboard-item\" style=\"grid-column: span 6; grid-row: span 2;\">\r\n"
				+ "        <label for=\"first_name\">Gyanadipta</label>\r\n"
				+ "        <input type=\"text\" id=\"first_name\" name=\"first_name\">\r\n"
				+ "        </input>\r\n"
				+ "    </div>\r\n"
				+ "    <div class=\"dashboard-item\" style=\"grid-column: span 6; grid-row: span 2;\">\r\n"
				+ "        <label for=\"gender\">Male</label>\r\n"
				+ "        <input type=\"text\" id=\"gender\" name=\"gender\">\r\n"
				+ "        </input>\r\n"
				+ "    </div>\r\n"
				+ "</div>\r\n"
				+ "\r\n"
				+ "</body>\r\n"
				+ "</html>\r\n";
	    // Render HTML content using Flying Saucer
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        ITextRenderer renderer = new ITextRenderer();
	        renderer.setDocumentFromString(htmlContent);
	        renderer.layout();
	        renderer.createPDF(outputStream);

	        // Reset the position of the output stream before writing to the file
//	        outputStream.reset();

	        // Specify the directory and file name where you want to save the PDF
	        String pdfFilePath = "/data/example.pdf";

	        String path = "/data";

	        Path directory = Paths.get(path);
	        if (!Files.exists(directory)) {
	            try {
	                Files.createDirectories(directory);
	            } catch (IOException e) {
	                e.printStackTrace();
	                // Handle directory creation failure appropriately
	            }
	        }

	        // Save the PDF to the specified file
	        try (FileOutputStream fileOutputStream = new FileOutputStream(pdfFilePath)) {
	            outputStream.writeTo(fileOutputStream);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exceptions appropriately
	    }
	}


	private String convertJsonToHtml(Map<String, Object> formData) {
		StringBuilder htmlBuilder = new StringBuilder("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n");
		htmlBuilder.append("    <meta charset=\"UTF-8\"></meta>\n");
		htmlBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\n");
		htmlBuilder.append(String.format("    <title>%s</title>\n", formData.get("name")));
		htmlBuilder.append("    <style>\n");
		htmlBuilder.append("        body {\n");
		htmlBuilder.append("            font-family: Arial, sans-serif;\n");
		htmlBuilder.append("            margin: 20px;\n");
		htmlBuilder.append("        }\n");
		htmlBuilder.append("        .dashboard {\n");
		htmlBuilder.append("            display: grid;\n");
		htmlBuilder.append("            grid-template-columns: repeat(24, 1fr);\n");
		htmlBuilder.append("            grid-template-rows: repeat(2, 1fr);\n");
		htmlBuilder.append("            gap: 10px;\n");
		htmlBuilder.append("        }\n");
		htmlBuilder.append("        .dashboard-item {\n");
		htmlBuilder.append("            border: 1px solid #ccc;\n");
		htmlBuilder.append("            padding: 10px;\n");
		htmlBuilder.append("            box-sizing: border-box;\n");
		htmlBuilder.append("        }\n");

		// Add styles for different classes (.text-center, .bold, .italic, .underline,
		// .larger) if needed

		htmlBuilder.append("    </style>\n</head>\n<body>\n\n<div class=\"dashboard\">\n");

		// Iterate through the dashboard array
		List<Map<String, Object>> dashboard = (List<Map<String, Object>>) formData.get("dashboard");
		for (Map<String, Object> element : dashboard) {
			htmlBuilder.append(generateFormField(element));
		}

		htmlBuilder.append("</div>\n\n</body>\n</html>");
		return htmlBuilder.toString();
	}

	private String generateFormField(Map<String, Object> fieldData) {
		StringBuilder fieldHtml = new StringBuilder();
		String type = (String) fieldData.get("type");

		if (type != null) {
			fieldHtml.append(
					String.format("    <div class=\"dashboard-item\" style=\"%s\">\n", getGridStyle(fieldData)));
			fieldHtml.append(String.format("        <label for=\"%s\">%s</label>\n", fieldData.get("fieldName"),
					fieldData.get("fieldtext")));

			switch (type) {
			case "text":
				fieldHtml.append(String.format("        <input type=\"text\" id=\"%s\" name=\"%s\" %s/>\n",
						fieldData.get("fieldName"), fieldData.get("fieldName"), getAdditionalAttributes(fieldData)));
				break;
			case "textarea":
				fieldHtml.append(
						String.format("        <textarea name=\"%s\"></textarea>\n", fieldData.get("fieldName")));
				break;
			case "select":
				fieldHtml.append(String.format("        <select name=\"%s\">\n", fieldData.get("fieldName")));
				fieldHtml.append(String.format("            <option>%s</option>\n", fieldData.get("fieldtext")));
				fieldHtml.append("        </select>\n");
				break;
			// Add more cases for other form field types as needed
			default:
				// Handle unknown field types or add more specific cases
			}

			fieldHtml.append("    </div>\n");
		}

		return fieldHtml.toString();
	}

	private String getGridStyle(Map<String, Object> fieldData) {
		int cols = (int) fieldData.get("cols");
		int rows = (int) fieldData.get("rows");
		int x = (int) fieldData.get("x");
		int y = (int) fieldData.get("y");

		return String.format("grid-column: span %d; grid-row: span %d; grid-column-start: %d; grid-row-start: %d;",
				cols, rows, x, y);
	}

	private String getAdditionalAttributes(Map<String, Object> fieldData) {
		StringBuilder attributes = new StringBuilder();

		for (Map.Entry<String, Object> entry : fieldData.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (!Arrays.asList("type", "fieldName", "fieldtext", "cols", "rows", "x", "y").contains(key)
					&& value != null) {
				attributes.append(String.format(" %s=\"%s\"", key, value));
			}
		}

		return attributes.toString();
	}
}
