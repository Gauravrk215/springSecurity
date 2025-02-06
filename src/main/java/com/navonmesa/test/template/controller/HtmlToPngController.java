package com.realnet.template.controller;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class HtmlToPngController {

//	@PostMapping(value = "/generate-file", produces = MediaType.TEXT_PLAIN_VALUE)
//	public ResponseEntity<String> generateFileFromHtml(@RequestParam String fileType, @RequestBody String htmlContent) {
//	    try {
//	        String fileExtension = "";
//	        String renderedContent = "";
//
//	        // Determine file extension and render content based on the fileType parameter
//	        if ("png".equalsIgnoreCase(fileType)) {
//	            fileExtension = "png";
//	            BufferedImage image = renderHtmlToImage(htmlContent);
//	            File outputfile = new File("output.png");
//	            ImageIO.write(image, "png", outputfile);
//	            renderedContent = "Image";
//	        } else {
//	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported file type.");
//	        }
//
//	        // Return the file path or URL in the response
//	        String filePath = "output." + fileExtension;
//	        if (!StringUtils.isEmpty(filePath)) {
//	            return ResponseEntity.ok().body(renderedContent + " saved successfully at: " + filePath);
//	        } else {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save the " + renderedContent + ".");
//	        }
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving the file.");
//	    }
//	}
//
//    private BufferedImage renderHtmlToImage(String htmlContent) {
//        JEditorPane editorPane = new JEditorPane();
//        editorPane.setContentType("text/html");
//        editorPane.setText(htmlContent);
//
//        // Wait for the component to be rendered
//        editorPane.setSize(new Dimension(1200, 800));
//        editorPane.setPreferredSize(new Dimension(1200, 800));
//        editorPane.setMinimumSize(new Dimension(1200, 800));
//        editorPane.setMaximumSize(new Dimension(1200, 800));
//
//        BufferedImage image = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = image.createGraphics();
//        editorPane.print(g2d);
//        g2d.dispose();
//
//        return image;
//    }
	
	
	 @PostMapping("/saveImageFromHtml")
	    public String saveImageFromHtml(@RequestBody String html) {
	        try {
	            // Parse HTML string
	            Document doc = Jsoup.parse(html);

	            // Find img elements
	            Elements imgElements = doc.select("img");

	            // Iterate through img elements
	            for (Element imgElement : imgElements) {
	                // Get Base64 encoded image data from src attribute
	                String src = imgElement.attr("src");
	                String base64Data = src.replaceFirst("^data:image/png;base64,", "");

	                // Decode Base64 string to byte array
	                byte[] imageData = Base64Utils.decodeFromString(base64Data);

	                // Write byte array to PNG file
	                FileOutputStream fos = new FileOutputStream("image.png");
	                fos.write(imageData);
	                fos.close();
	            }

	            return "Image saved successfully!";
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "Failed to save image!";
	        }
	    }
	
	 
	 @PostMapping("/saveImageFromHtmlFileType")
	    public String saveImageFromHtml(
	            @RequestParam String fileType,
	            @RequestBody String html
	    ) {
	        try {
	            // Parse HTML string
	            Document doc = Jsoup.parse(html);

	            // Find img elements
	            Elements imgElements = doc.select("img");

	            // Iterate through img elements
	            for (Element imgElement : imgElements) {
	                // Get Base64 encoded image data from src attribute
	                String src = imgElement.attr("src");
	                String base64Data = src.replaceFirst("^data:image/png;base64,", "");

	                // Decode Base64 string to byte array
	                byte[] imageData = Base64Utils.decodeFromString(base64Data);

	                // Determine file extension and content type
	                String fileExtension;
	                String contentType;
	                if ("png".equalsIgnoreCase(fileType)) {
	                    fileExtension = ".png";
	                    contentType = "image/png";
	                } else if ("svg".equalsIgnoreCase(fileType)) {
	                    fileExtension = ".svg";
	                    contentType = "image/svg+xml";
	                } else {
	                    return "Unsupported file type!";
	                }

	                // Write byte array to file
	                String filename = "image" + fileExtension;
	                FileOutputStream fos = new FileOutputStream(filename);
	                fos.write(imageData);
	                fos.close();
	            }

	            return "Image saved successfully!";
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "Failed to save image!";
	        }
	    }
}