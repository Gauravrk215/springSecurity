package com.realnet.BulkUpload.Controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.realnet.BulkUpload.Entity.BulkUpload_t;
import com.realnet.BulkUpload.Repository.BulkUpload_Repository;
import com.realnet.BulkUpload.Services.BulkUpload_Service;
import com.realnet.template.entity.TemplateFileUpload;
import com.realnet.template.repository.TemplatedataRepo;
import com.realnet.template.service.DynamicTemplateService;
import com.realnet.template.service.FileUploadService;

@RestController
@RequestMapping("/masterimport")
public class MasterImportController {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TemplatedataRepo temprepo;
	@Autowired
	private DynamicTemplateService dynamicTemplateService;

	@Autowired
	private BulkUpload_Service bulkUpload_Service;

	@Autowired
	private BulkUpload_Repository bulkUpload_Repository;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${projectPath}")
	private String projectPath;

	@GetMapping("/demo/download/{file_type}")
	public ResponseEntity<?> demoTemplate(@PathVariable String file_type) throws IOException {
		Workbook workbook = new XSSFWorkbook();

		if (file_type.equalsIgnoreCase("Customer")) {
			// Create Customer sheet with headers only
			String[] customerHeaders = { "balance", "c_status", "city", "country","currency_code","customer_id",
					"date_of_birth", "defaultsite_id", "email", "entity_name","file_name","file_path",
					"first_name", "gender", "gst_no", "gst_state","house_no","mobile_no",
					"pan_no", "sales_rep", "special_price", "state","street_address","street_address2",
					"time_zone", "postal_code", "contact_number"};
			createSheetWithHeaders(workbook, "Customer", customerHeaders);

			// Create Site sheet with headers only
			String[] siteHeaders = { "Site Name", "Site Description", "Active", "Test" };
			createSheetWithHeaders(workbook, "Site", siteHeaders);
		} else {
			return new ResponseEntity<String>("Not found", HttpStatus.BAD_REQUEST);
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file_type + "_template.xlsx")
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(out.toByteArray());
	}

	private void createSheetWithHeaders(Workbook workbook, String sheetName, String[] headers) {
		Sheet sheet = workbook.createSheet(sheetName);
		Row headerRow = sheet.createRow(0);

		// Create headers
		for (int col = 0; col < headers.length; col++) {
			Cell cell = headerRow.createCell(col);
			cell.setCellValue(headers[col]);
		}
	}

	@PostMapping("/DownloadExcelForMaster/{id}")
	public ResponseEntity<?> DownloadExcelForMaster(@PathVariable Long id,
			@RequestBody Map<String, List<Map<String, Object>>> jsonData) {
		try {

			List<TemplateFileUpload> templateFileUpload = temprepo.findUnprocessedRecordsOrderedByIdAsc();
			
			

			if (!templateFileUpload.isEmpty()) {
				for (TemplateFileUpload upload : templateFileUpload) {
					String entity_name = upload.getEntity_name();
					BulkUpload_t bulkUpload_t = bulkUpload_Repository.getentityName(entity_name);
					String ruleLine = bulkUpload_t.getRule_line();

//		BulkUpload_t bulkUpload_t = bulkUpload_Repository.getentityName(entity_name);
//		String ruleLine = bulkUpload_t.getRule_line();

					JsonParser parser = new JsonParser();
					JsonElement element = parser.parse(ruleLine);
					JsonArray array = element.getAsJsonArray();
					Iterator<JsonElement> iterator = array.iterator();

					String fromsheet = null;
					String fromColumn = null;
					String validationTable = null;
					String checkColumn = null;
					String useColumn = null;
					String replacementtable = null;
					String replacementcolumn = null;

					while (iterator.hasNext()) {
						JsonElement next = iterator.next();
						JsonObject jsonObject = next.getAsJsonObject();

						fromsheet = jsonObject.get("fromsheet").getAsString();
						fromColumn = jsonObject.get("fromColumn").getAsString();
						validationTable = jsonObject.get("validationTable").getAsString();
						checkColumn = jsonObject.get("checkColumn").getAsString();
						useColumn = jsonObject.get("useColumn").getAsString();
						replacementtable = jsonObject.get("useTable").getAsString();
						replacementcolumn = jsonObject.get("replacementcolumn").getAsString();

						break;
					}

					String customerTableName = validationTable;
					String siteTableName = replacementtable;

					Set<String> tableNames = jsonData.keySet(); // Get all unique table names

					String siteEntityName = null;
					for (String tableName : tableNames) {
						List<Map<String, Object>> tableData = jsonData.get(tableName);

						// Process tableData based on the tableName (e.g., "Site" or "Customer")
						System.out.println("Table Name: " + tableName);
						for (Map<String, Object> row : tableData) {
							// Process individual rows within the table data
							System.out.println("Row Data: " + row);
						}
					}

					List<Map<String, Object>> processedDataList = new ArrayList<>(); // List to hold processed data rows

					// Iterate through each customer data entry
					List<Map<String, Object>> customerData = jsonData.get("Customer");
					for (Map<String, Object> insertCustomerData : customerData) {
						String customerName = (String) insertCustomerData.get(checkColumn);

						// Check if the customerName is not null and iterate through "Site" data
						List<Map<String, Object>> siteData = jsonData.get(fromsheet);

						List<Map<String, Object>> matchedSiteData = new ArrayList<>();
						if (customerName != null) {
							// Iterate through "Site" data and check for a matching "entity_name"
							for (Map<String, Object> siteRow : siteData) {
								// Specify the index as "AM" (39th column)
//	                    String columnIndex = "AM";
								String columnIndex = fromColumn;

								// Retrieve the value at the specified index

								for (Map.Entry<String, Object> entry : siteRow.entrySet()) {
									if (entry.getKey().equals(columnIndex)) {
										siteEntityName = (String) entry.getValue();
										break; // Exit the loop once the value is found
									}
								}

								if (customerName.equals(siteEntityName)) {
									// Add the matching "Site" data to the list
									matchedSiteData.add(siteRow);
								}
							}
						}

						ObjectMapper objectMapper = new ObjectMapper();
						String insertCustomerDataJson = objectMapper.writeValueAsString(insertCustomerData);
						String matchedSiteDataJson = objectMapper.writeValueAsString(matchedSiteData);

						String customerSql = getInsertQuery(customerTableName, insertCustomerData);

						// Insert data into the customer table
						Object[] customerValues = new Object[insertCustomerData.size() + 5];
						int index = 0;
						for (Object value : insertCustomerData.values()) {
							customerValues[index++] = value;
						}
						customerValues[index++] = new Timestamp(System.currentTimeMillis()); // created_at
						customerValues[index++] = null; // created_by
						customerValues[index++] = null; // updated_by
						customerValues[index++] = new Timestamp(System.currentTimeMillis()); // updated_at
						customerValues[index] = null; // account_id
						jdbcTemplate.update(customerSql, customerValues);

						// we can use useColumn here
						Long generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

						List<Map<String, Object>> insertMatchedSiteData = new ArrayList<>(); // List to hold processed
																								// data rows

						for (Map<String, Object> siteRow : matchedSiteData) {
							// Replace "Customer Name" with "customer_master_id" if it exists
							if (siteRow.containsKey(siteEntityName)) {
								siteRow.put(replacementcolumn, generatedId);
								siteRow.remove(siteEntityName);
							}
							insertMatchedSiteData.add(siteRow);
						}

						for (Map<String, Object> siteRow : insertMatchedSiteData) {
							Object[] siteValues = new Object[siteRow.size() + 5]; // Create a new array for each row

							int siteIndex = 0;
							for (Object value : siteRow.values()) {
								siteValues[siteIndex++] = value;
							}

							siteValues[siteIndex++] = new Timestamp(System.currentTimeMillis()); // created_at
							siteValues[siteIndex++] = null; // created_by
							siteValues[siteIndex++] = null; // updated_by
							siteValues[siteIndex++] = new Timestamp(System.currentTimeMillis()); // updated_at
							siteValues[siteIndex] = null; // account_id

							String siteSql = getInsertQuery(siteTableName, siteRow);
							jdbcTemplate.update(siteSql, siteValues);
						}

						// Add the processed customer data to the list
						processedDataList.add(insertCustomerData);
					}

					// Use a LinkedHashMap to preserve insertion order in the response
					Map<String, List<Map<String, Object>>> response = new LinkedHashMap<>();
					response.put("result", processedDataList);

					return new ResponseEntity<>(response, HttpStatus.OK);

				}
			}
			return new ResponseEntity<>("No data to process", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions and return an appropriate response
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private List<String> getColumnNames(Map<String, Object> dataRow) {
		return new ArrayList<>(dataRow.keySet());
	}

	// Method to write data to a sheet
	private void writeDataToSheet(List<Map<String, Object>> dataList, XSSFSheet sheet, List<String> columnNames) {
		int rowIndex = 0;
		// Create the header row
		XSSFRow headerRow = sheet.createRow(rowIndex++);
		for (int i = 0; i < columnNames.size(); i++) {
			XSSFCell cell = headerRow.createCell(i);
			cell.setCellValue(columnNames.get(i));
		}
		// Add "Status" and "Exception" columns to the sheet
		headerRow.createCell(columnNames.size()).setCellValue("Status");
		headerRow.createCell(columnNames.size() + 1).setCellValue("Exception");

		// Write the data rows
		for (Map<String, Object> row : dataList) {
			XSSFRow dataRow = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			for (String columnName : columnNames) {
				Object value = row.get(columnName);
				XSSFCell cell = dataRow.createCell(columnIndex++);
				cell.setCellValue(value != null ? value.toString() : "");
			}
			// Add "Status" and "Exception" values to the data row
			dataRow.createCell(columnIndex++).setCellValue(row.get("Status").toString());
			dataRow.createCell(columnIndex).setCellValue(row.get("Exception").toString());
		}

	}

	private String extractExceptionMessage(DataIntegrityViolationException e) {
		String errorMessage = e.getMessage();
		int startIndex = errorMessage.indexOf("Incorrect ");
		int endIndex = errorMessage.indexOf("at row");
		if (startIndex != -1 && endIndex != -1) {
			return errorMessage.substring(startIndex, endIndex).trim();
		}
		return errorMessage;
	}

	private String getInsertQuery(String tableName, Map<String, Object> data) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("INSERT INTO ");
		sqlBuilder.append(tableName);
		sqlBuilder.append(" (");
		sqlBuilder.append(String.join(", ", data.keySet()));
		sqlBuilder.append(", created_at, created_by, updated_by, updated_at, account_id) VALUES (");
		sqlBuilder.append(String.join(", ", Collections.nCopies(data.size(), "?")));
		sqlBuilder.append(", ?, ?, ?, ?, ?)");
		return sqlBuilder.toString();
	}

	@GetMapping("/sheet/{id}")
	public ResponseEntity<?> getSheet(@PathVariable Long id) throws IOException {
		ResponseEntity<?> jsonData = convertFileToJsonforsheet(id);

		return ResponseEntity.ok(jsonData.getBody());
	}

	public ResponseEntity<?> convertFileToJsonforsheet(Long id) throws IOException {
		// Retrieve the TemplateFileUpload entity based on the provided ID
		Optional<TemplateFileUpload> fileUploadOptional = temprepo.findById(id);

		if (!fileUploadOptional.isPresent()) {
			// Handle the case where the file is not found in the specified location
			return ResponseEntity.notFound().build();
		}

		TemplateFileUpload fileUpload = fileUploadOptional.get();
		String location = fileUpload.getFile_location();
		String fileName = fileUpload.getFile_changed_name() + ".xlsx";
		String filePath = location + File.separator + "processingfile" + File.separator + fileName;
		File file = new File(filePath);

		if (!file.exists()) {
			// Handle the case where the file is not found in the specified location
			return ResponseEntity.notFound().build();
		}

		try (FileInputStream fis = new FileInputStream(file)) {
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			List<String> sheetNames = new ArrayList<>();

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				sheetNames.add(sheetName);
			}

			return ResponseEntity.ok(sheetNames);
		} catch (Exception e) {
			// Handle any exceptions that may occur during processing
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Excel file.");
		}
	}

	@GetMapping("/downloadImportStatement/{id}")
	public ResponseEntity<?> downloadImportStatement(@PathVariable Long id) {
		// Retrieve the file data from the database based on the ID
		TemplateFileUpload fileUpload = fileUploadService.getFileById(id);

		// Get the file location from the entity
		String fileLocation = fileUpload.getDownloadfileLocation();
//		String filename = fileUpload.getDownloadfileName();
//
//		String filepath = fileLocation + File.separator + filename;

		try {
			// Read the file content from the specified location
			byte[] fileContent = Files.readAllBytes(Paths.get(fileLocation));

			// Create a ByteArrayResource from the file content
			ByteArrayResource resource = new ByteArrayResource(fileContent);

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + fileUpload.getFile_name() + "\"")
					.body(resource);
		} catch (IOException e) {
			// Handle any potential exceptions (e.g., file not found)
			e.printStackTrace();
			return ResponseEntity.status(500).body("Error occurred while reading the file.");
		}
	}

	@GetMapping("/columns/{tableName}")
	public List<String> getColumnNames(@PathVariable String tableName) {
		Set<String> columnNamesSet = new HashSet<>();
		String convertedTableName = tableName;
		List<String> columnNames = jdbcTemplate.queryForList(
				"SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?",
				new Object[] { convertedTableName }, String.class);
		// Exclude specific column names
		List<String> excludedColumns = Arrays.asList("id", "account_id", "updated_at", "created_at", "created_by",
				"updated_by");
		for (String columnName : columnNames) {
			if (!excludedColumns.contains(columnName)) {
				columnNamesSet.add(columnName);
			}
		}
		return new ArrayList<>(columnNamesSet);
	}

	private String getCellValue(Map<String, List<Map<String, Object>>> jsonData, String sheetName, String cellAddress)
			throws IOException {
		if (jsonData.containsKey(sheetName)) {
			List<Map<String, Object>> sheetData = jsonData.get(sheetName);
			int size = sheetData.size();
			System.out.println(size);
			Map<String, Object> firstRowData = sheetData.get(0);

			// Separate the numeric part (row part) and non-numeric part (column part)
			String rowPart = "";
			String columnPart = "";

			for (char c : cellAddress.toCharArray()) {
				if (Character.isDigit(c)) {
					rowPart += c;
				} else {
					columnPart += c;
				}
			}

			System.out.println("Column Part: " + columnPart);
			System.out.println("Row Part: " + rowPart);

			int columnIndex = getColumnIndex(columnPart); // Get the column index based on the columnPart
//	        int rowIndex = Integer.parseInt(rowPart) - 1; // Excel-style 1-based index
			System.out.println("Column Index: " + columnIndex);

			if (columnIndex >= 0) {
				// Get the list of keys from the firstRowData map
				List<String> keys = new ArrayList<>(firstRowData.keySet());

				// Check if the columnIndex is within the range of keys
				if (columnIndex < keys.size()) {
					String columnName = keys.get(columnIndex);
					return columnName;
				}
			}
		}

		return null;
	}

	private int getColumnIndex(String columnPart) {
		int index = 0;
		int multiplier = 1;

		// Iterate through the columnPart in reverse order (right to left)
		for (int i = columnPart.length() - 1; i >= 0; i--) {
			char c = columnPart.charAt(i);
			index += (c - 'A' + 1) * multiplier;
			multiplier *= 26; // Multiply by 26 for each position to calculate the index
		}

		return index - 1; // Subtract 1 to get a 0-based index
	}

	@PostMapping("/DownloadExcel/{entityName}")
	public ResponseEntity<Resource> importdatadownloadexcel(@PathVariable String entityName,
			@RequestBody List<Map<String, Object>> data) throws IOException {
		List<Map<String, Object>> processedData = processData(entityName, data);
		if (!processedData.isEmpty()) {
			Workbook workbook = createWorkbook(processedData);
			Resource resource = createExcelResource(workbook);
			return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=failed_records.xlsx").body(resource);
		}
		return ResponseEntity.ok().body(null);
	}

	private List<Map<String, Object>> processData(String entityName, List<Map<String, Object>> data) {
		List<Map<String, Object>> processedData = new ArrayList<>();
		try {
			String convertedTableName = entityName;
			String sql = getInsertQuery(convertedTableName, data.get(0));
			for (int i = 0; i < data.size(); i++) {
				Map<String, Object> row = data.get(i);
				Map<String, Object> processedRow = new HashMap<>();
				try {
					Object[] values = new Object[row.size() + 5]; // +5 for the additional columns
					int index = 0;
					for (Object value : row.values()) {
						values[index++] = value;
					}
					values[index++] = new Timestamp(System.currentTimeMillis()); // created_at
					values[index++] = null; // created_by
					values[index++] = null; // updated_by
					values[index++] = new Timestamp(System.currentTimeMillis()); // updated_at
					values[index] = null; // account_id
					jdbcTemplate.update(sql, values);
					processedRow.putAll(row);
					processedRow.put("Status", "Success");
					processedRow.put("Exception", "NA");
				} catch (DataIntegrityViolationException e) {
					processedRow.putAll(row);
					processedRow.put("Status", "Error");
					String exceptionMessage = extractExceptionMessage(e);
					processedRow.put("Exception", exceptionMessage);
				}
				processedData.add(processedRow);
			}
		} catch (Exception e) {
			// Handle any exceptions here if needed
		}
		return processedData;
	}

	private Workbook createWorkbook(List<Map<String, Object>> processedData) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Failed Records");

		// Add column headers to the worksheet
		Row headerRow = sheet.createRow(0);
		int columnIndex = 0;
		for (String key : processedData.get(0).keySet()) {
			Cell cell = headerRow.createCell(columnIndex++);
			cell.setCellValue(key);
		}

		// Add data rows to the worksheet
		int rowIndex = 1;
		for (Map<String, Object> row : processedData) {
			Row dataRow = sheet.createRow(rowIndex++);
			columnIndex = 0;
			for (Object value : row.values()) {
				Cell cell = dataRow.createCell(columnIndex++);
				cell.setCellValue(value.toString());
			}
		}

		return workbook;
	}

	private Resource createExcelResource(Workbook workbook) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		return new InputStreamResource(inputStream);
	}
	
	
	
	
//	private ResponseEntity<?> getData(@PathVariable String type ){
//		if(type.equalsIgnoreCase("Customer")) {
//			
//			
//			String[] customerHeaders = { "balance", "c_status", "city", "country","currency_code","customer_id",
//					"date_of_birth", "defaultsite_id", "email", "entity_name","file_name","file_path",
//					"first_name", "gender", "gst_no", "gst_state","house_no","mobile_no",
//					"pan_no", "sales_rep", "special_price", "state","street_address","street_address2",
//					"time_zone", "postal_code", "contact_number"};
//			
//			
//		}
//		
//	}

}
