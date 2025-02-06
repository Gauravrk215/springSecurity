package com.realnet.template.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.realnet.template.entity.TemplateFileUpload;
import com.realnet.template.repository.TemplatedataRepo;
import com.realnet.template.service.DynamicTemplateService;

@RestController
@RequestMapping("api/dynamic")
public class DynamicTemplateController {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TemplatedataRepo temprepo;
	@Autowired
	private DynamicTemplateService dynamicTemplateService;

	@PostMapping("/templatetojsondata")
	public ResponseEntity<List<JSONObject>> saveTemplateInJson(@RequestParam("file") MultipartFile file)
			throws IOException {
		List<JSONObject> jsonList = dynamicTemplateService.processTemplate(file);
		return new ResponseEntity<>(jsonList, HttpStatus.OK);
	}

	@PostMapping("/templatetojson/{id}")
	public ResponseEntity<?> convertFileToJson(@PathVariable Long id) throws IOException {
		// Retrieve the TemplateFileUpload entity based on the provided ID
		Optional<TemplateFileUpload> fileUploadOptional = temprepo.findById(id);

		TemplateFileUpload fileUpload = fileUploadOptional.get();
		String location = fileUpload.getFile_location();
		String fileName = fileUpload.getFile_changed_name() + ".xlsx";
		String filePath = location + File.separator + "processingfile" + File.separator + fileName;
		File file = new File(filePath);
		if (!file.exists()) {
			// Handle the case where the file is not found in the specified location
			return ResponseEntity.notFound().build();
		}
		MultipartFile multipartFile = new MockMultipartFile(fileName, new FileInputStream(file));
		List<JSONObject> jsonList = dynamicTemplateService.processTemplate(multipartFile);
		return ResponseEntity.ok(jsonList);

	}

	@GetMapping("/getHeaders/{id}")
	public ResponseEntity<List<String>> getFileHeaders(@PathVariable Long id) throws IOException {
		Optional<TemplateFileUpload> fileUploadOptional = temprepo.findById(id);

		TemplateFileUpload fileUpload = fileUploadOptional.get();
		String location = fileUpload.getFile_location();
		String fileName = fileUpload.getFile_changed_name() + ".xlsx";
		String filePath = location + File.separator + "processingfile" + File.separator + fileName;
		File file = new File(filePath);
		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		}
		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(0);
		Row headerRow = sheet.getRow(0);
		List<String> headers = new ArrayList<>();
		for (Cell cell : headerRow) {
			String header = cell.getStringCellValue();
			headers.add(header);
		}
		workbook.close();
		return ResponseEntity.ok(headers);
	}

	@PostMapping("/jsonkeychange")
	public ResponseEntity<?> changeKeysOfJson(@RequestParam String newjson, @RequestParam String body)
			throws IOException {
		JsonParser parser = new JsonParser();
		JsonArray newKeysArray = parser.parse(newjson).getAsJsonArray();
		JsonElement bodyElement = parser.parse(body);
		JsonArray bodyArray = bodyElement.getAsJsonArray();
		List<Map<String, Object>> jsonList = new ArrayList<>(); // Use Object type to handle booleans
		for (JsonElement bodyElement1 : bodyArray) {
			JsonObject bodyObject = bodyElement1.getAsJsonObject();
			Map<String, Object> map = new HashMap<>(); // Use Object type to handle booleans
			for (JsonElement newKeyElement : newKeysArray) {
				JsonObject newKeyObject = newKeyElement.getAsJsonObject();
				String originalKey = newKeyObject.get("headerName").getAsString();
				String newKey = newKeyObject.get("value").getAsString();
				if (bodyObject.has(originalKey)) {
					JsonElement valueElement = bodyObject.get(originalKey);
					// Check if the value is a string representation of boolean
					if (valueElement.isJsonPrimitive() && valueElement.getAsJsonPrimitive().isString()) {
						String value = valueElement.getAsString().toLowerCase(); // Convert to lowercase
						// Convert "true" or "false" to boolean type
						if (value.equals("true") || value.equals("false")) {
							boolean boolValue = Boolean.parseBoolean(value);
							map.put(newKey, boolValue);
							continue; // Skip further processing for this key
						}
					}
					String value = valueElement.getAsString();
					map.put(newKey, value);
				}
			}
			jsonList.add(map);
		}
		return new ResponseEntity<>(jsonList, HttpStatus.ACCEPTED);
	}

	@PostMapping("/DownloadExcel/{entityName}")
	public ResponseEntity<Resource> importdatadownloadexcel(@PathVariable String entityName,
			@RequestBody List<Map<String, Object>> data) {
		List<Map<String, Object>> processedData = new ArrayList<>();
		try {
			String convertedTableName = entityName;
			String sql = getInsertQuery1(convertedTableName, data.get(0));
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
					String exceptionMessage = extractExceptionMessage1(e);
					processedRow.put("Exception", exceptionMessage);
				}
				processedData.add(processedRow);
			}
			if (!processedData.isEmpty()) {
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
				// Write the workbook to a file
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
				headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=failed_records.xlsx");
				Resource resource = new InputStreamResource(inputStream);
				return ResponseEntity.ok().headers(headers).body(resource);
			}
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	private String extractExceptionMessage1(DataIntegrityViolationException e) {
		String errorMessage = e.getMessage();
		int startIndex = errorMessage.indexOf("Incorrect ");
		int endIndex = errorMessage.indexOf("at row");
		if (startIndex != -1 && endIndex != -1) {
			return errorMessage.substring(startIndex, endIndex).trim();
		}
		return errorMessage;
	}

	private String convertTableName1(String tableName) {
		switch (tableName.toLowerCase()) {
		case "priority":
			return "sr_priority2_t";
		case "impact":
			return "sr_impact2_t";
		case "urgency":
			return "Sr_urgency_t";
		case "category":
			return "sr_category2_t";
		case "state":
			return "sr_state_t";
		case "contact_type":
			return "sr_contact_type_t";
		case "customer":
			return "sr_customer_t";
		case "handler":
			return "sr_handler_t";
		default:
			return tableName;
		}
	}

	private String getInsertQuery1(String tableName, Map<String, Object> data) {
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

	private void createTableIfNotExists(String tableName, Map<String, Object> data) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE IF NOT EXISTS ");
		sqlBuilder.append(tableName);
		sqlBuilder.append(" (");
		// Check if "id" field exists in the data map
		if (data.containsKey("id")) {
			// Add "id" column as the first column
			sqlBuilder.append("id INT PRIMARY KEY, ");
		}
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			String columnName = entry.getKey();
			Object columnValue = entry.getValue();
			// Skip adding "id" field again
			if (columnName.equalsIgnoreCase("id")) {
				continue;
			}
			if (columnName.equalsIgnoreCase("date") || columnName.equalsIgnoreCase("dob")) {
				sqlBuilder.append(columnName).append(" DATE, ");
			} else {
				sqlBuilder.append(columnName).append(" VARCHAR(255), ");
			}
		}
		sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length()); // Remove the last comma and space
		sqlBuilder.append(")");
		jdbcTemplate.execute(sqlBuilder.toString());
	}

	@GetMapping("/columns/{tableName}")
	public List<String> getColumnNames(@PathVariable String tableName) {
		Set<String> columnNamesSet = new HashSet<>();
		String convertedTableName = convertTableName(tableName);
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

	private String convertTableName(String tableName) {
		switch (tableName.toLowerCase()) {
		case "customer":
			return "customer_master_t";
		case "impact":
			return "sr_impact2_t";
		case "urgency":
			return "Sr_urgency_t";
		case "category":
			return "sr_category2_t";
		case "state":
			return "sr_state_t";
		case "contact_type":
			return "sr_contact_type_t";
		
		case "handler":
			return "sr_handler_t";
		default:
			return tableName;
		}
	}

}
