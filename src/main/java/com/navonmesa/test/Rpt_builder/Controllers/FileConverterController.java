package com.realnet.Rpt_builder.Controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;


@RestController
@RequestMapping("/rbbuilder/fileconverter")
public class FileConverterController {

//	 @PostMapping("/downloadCsv")
//	    public ResponseEntity<String> downloadCSV(@RequestBody List<Map<String, Object>> dataList) {
//	        try {
//	            // Convert dataList to CSV format and provide download
//	            String csvContent = convertToCSV(dataList);
//
//	            // Set headers for response
//	            HttpHeaders headers = new HttpHeaders();
//	            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");
//
//	            // Send the CSV content as a response
//	            return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
//	        } catch (IOException e) {
//	            // Handle exception
//	            return new ResponseEntity<>("Error occurred while processing data", HttpStatus.INTERNAL_SERVER_ERROR);
//	        }
//	    }
//
//	    private String convertToCSV(List<Map<String, Object>> dataList) throws IOException {
//	        StringWriter writer = new StringWriter();
//	        CSVWriter csvWriter = new CSVWriter(writer);
//
//	        // Write headers
//	        if (!dataList.isEmpty()) {
//	            Map<String, Object> firstRow = dataList.get(0);
//	            String[] headers = firstRow.keySet().toArray(new String[0]);
//	            csvWriter.writeNext(headers);
//
//	            // Write data
//	            for (Map<String, Object> data : dataList) {
//	                String[] rowData = new String[headers.length];
//	                for (int i = 0; i < headers.length; i++) {
//	                    Object value = data.get(headers[i]);
//	                    rowData[i] = (value != null) ? value.toString() : "";
//	                }
//	                csvWriter.writeNext(rowData);
//	            }
//	        }
//
//	        // Close CSV writer
//	        csvWriter.close();
//
//	        return writer.toString();
//	    }

	@PostMapping("/downloadFile/{format}")
	public ResponseEntity<?> downloadFile(@RequestBody List<Map<String, Object>> dataList,@PathVariable String format) {
		try {
			// Convert dataList to the requested format
			byte[] fileContent;
			String fileName;
			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

			 if ("csv".equalsIgnoreCase(format)) {
		            fileContent = convertToCSV(dataList).getBytes();
		            fileName = "data_" + timestamp + ".csv";
		        } else if ("excel".equalsIgnoreCase(format)) {
		            fileContent = convertToExcel(dataList);
		            fileName = "data_" + timestamp + ".xlsx";
		        } else if ("pdf".equalsIgnoreCase(format)) {
		            fileContent = convertToPDF(dataList);
		            fileName = "data_" + timestamp + ".pdf";
		        } else {
		            return new ResponseEntity<>("Unsupported file format", HttpStatus.BAD_REQUEST);
		        }
			// Set headers for response
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

			// Send the file content as a response
			return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
		} catch (IOException e) {
			// Handle exception
			return new ResponseEntity<>("Error occurred while processing data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String convertToCSV(List<Map<String, Object>> dataList) throws IOException {
		StringWriter writer = new StringWriter();
		CSVWriter csvWriter = new CSVWriter(writer);

		// Write headers
		if (!dataList.isEmpty()) {
			Map<String, Object> firstRow = dataList.get(0);
			String[] headers = firstRow.keySet().toArray(new String[0]);
			csvWriter.writeNext(headers);

			// Write data
			for (Map<String, Object> data : dataList) {
				String[] rowData = new String[headers.length];
				for (int i = 0; i < headers.length; i++) {
					Object value = data.get(headers[i]);
					rowData[i] = (value != null) ? value.toString() : "";
				}
				csvWriter.writeNext(rowData);
			}
		}

		// Close CSV writer
		csvWriter.close();

		return writer.toString();
	}

	private byte[] convertToExcel(List<Map<String, Object>> dataList) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Data");

		// Write headers
		if (!dataList.isEmpty()) {
			Row headerRow = sheet.createRow(0);
			Map<String, Object> firstRow = dataList.get(0);
			int cellIndex = 0;
			for (String header : firstRow.keySet()) {
				Cell cell = headerRow.createCell(cellIndex++);
				cell.setCellValue(header);
			}

			// Write data
			int rowIndex = 1;
			for (Map<String, Object> data : dataList) {
				Row dataRow = sheet.createRow(rowIndex++);
				int columnIndex = 0;
				for (Object value : data.values()) {
					Cell cell = dataRow.createCell(columnIndex++);
					if (value != null) {
						if (value instanceof Number) {
							cell.setCellValue(((Number) value).doubleValue());
						} else {
							cell.setCellValue(value.toString());
						}
					}
				}
			}
		}

		// Convert workbook to byte array
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return outputStream.toByteArray();
	}

	 private byte[] convertToPDF(List<Map<String, Object>> dataList) throws IOException {
	        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	             PDDocument document = new PDDocument()) {

	            PDPage page = new PDPage();
	            document.addPage(page);

	            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
	                // Set font and size (you can customize these)
	                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	                float margin = 50;
	                float yStart = page.getMediaBox().getHeight() - margin;
	                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
	                float yPosition = yStart;

	                // Write headers
	                Map<String, Object> firstRow = dataList.get(0);
	                int numberOfColumns = firstRow.size();
	                float tableHeight = 20f; // Height of the table (you can adjust this)

	                // Set column widths
	                float[] columnWidths = new float[numberOfColumns];
	                float tableHeightStart = yStart - 2 * tableHeight;
	                float nextXStart = margin;
	                for (int i = 0; i < numberOfColumns; i++) {
	                    columnWidths[i] = tableWidth / (float) numberOfColumns;
	                }

	                // Write data
	                for (Map<String, Object> data : dataList) {
	                    contentStream.beginText();
	                    contentStream.newLineAtOffset(nextXStart, yPosition);
	                    int columnIndex = 0;
	                    for (Object value : data.values()) {
	                        contentStream.showText(value != null ? value.toString() : "");
	                        contentStream.newLineAtOffset(columnWidths[columnIndex], 0);
	                        columnIndex++;
	                    }
	                    contentStream.endText();
	                    yPosition -= tableHeight;
	                }
	            }

	            document.save(byteArrayOutputStream);
	            return byteArrayOutputStream.toByteArray();
	        } catch (IOException e) {
	            // Handle PDF creation exception
	            return new byte[0];
	        }
	    }
}
