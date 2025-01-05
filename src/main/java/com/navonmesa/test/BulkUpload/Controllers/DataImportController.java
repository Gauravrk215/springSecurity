package com.realnet.BulkUpload.Controllers;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realnet.BulkUpload.Repository.BulkUpload_Repository;
import com.realnet.BulkUpload.Services.BulkUpload_Service;
import com.realnet.template.entity.TemplateFileUpload;
import com.realnet.template.repository.TemplatedataRepo;
import com.realnet.template.service.DynamicTemplateService;
import com.realnet.template.service.FileUploadService;

@RestController
@RequestMapping("api/BulkUpload")
public class DataImportController {
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

		try (FileInputStream fis = new FileInputStream(file)) {
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			LinkedHashMap<String, List<Map<String, String>>> result = new LinkedHashMap<>();

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();

				List<Map<String, String>> sheetDataList = new ArrayList<>();

				XSSFRow headerRow = sheet.getRow(0);
				for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
					XSSFRow row = sheet.getRow(rowNum);

					if (row != null) {
						LinkedHashMap<String, String> rowData = new LinkedHashMap<>();
						for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
							XSSFCell cell = row.getCell(cellNum);
							if (cell != null) {
								String columnName = headerRow.getCell(cellNum).getStringCellValue();
								String cellValue;

								// Check if the cell contains a date
								if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
									// If it's a date, format it as "dd-MMM-yyyy"
									Date dateCellValue = cell.getDateCellValue();
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
									cellValue = dateFormat.format(dateCellValue);
								} else {
									// If not a date, convert the cell value to a string
									cellValue = cell.toString();
								}

								rowData.put(columnName, cellValue);
							}
						}
						sheetDataList.add(rowData);
					}
				}

				// Add the sheet data to the result LinkedHashMap with the sheet name as the key
				result.put(sheetName, sheetDataList);
			}

			return ResponseEntity.ok(result);
		} catch (Exception e) {
			// Handle any exceptions that may occur during processing
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Excel file.");
		}
	}

	@GetMapping("/getHeaders/{id}")
	public ResponseEntity<?> getAllSheetHeaders(@PathVariable Long id) throws IOException {
		Optional<TemplateFileUpload> fileUploadOptional = temprepo.findById(id);

		TemplateFileUpload fileUpload = fileUploadOptional.orElse(null);

		if (fileUpload == null) {
			return ResponseEntity.notFound().build();
		}

		String location = fileUpload.getFile_location();
		String fileName = fileUpload.getFile_changed_name() + ".xlsx";
		String filePath = location + File.separator + "processingfile" + File.separator + fileName;
		File file = new File(filePath);

		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		}

		try (Workbook workbook = WorkbookFactory.create(file)) {
			Map<String, List<Map<String, String>>> sheetHeadersMap = new LinkedHashMap<>();

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				List<Map<String, String>> sheetHeaders = new ArrayList<>();

				Row headerRow = sheet.getRow(0);

				if (headerRow != null) {
					for (Cell cell : headerRow) {
						String header = cell.getStringCellValue();
						Map<String, String> headerObject = new LinkedHashMap<>();
						headerObject.put("headerName", header);
						headerObject.put("value", ""); // Empty value
						sheetHeaders.add(headerObject);
					}
				}

				sheetHeadersMap.put(sheet.getSheetName(), sheetHeaders);
			}

			return ResponseEntity.ok(sheetHeadersMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Excel file.");
		}
	}

	@PostMapping("/jsonkeychange")
	public ResponseEntity<List<Map<String, List<Map<String, Object>>>>> transformData(
			@RequestParam("updatedKeyJson") String updatedKeyJson,
			@RequestParam("oldKeyDataJson") String oldKeyDataJson) throws IOException {
		try {
			// Deserialize JSON strings into JSON objects
			JsonNode updatedKeyJsonObject = objectMapper.readTree(updatedKeyJson);
			JsonNode oldKeyDataJsonArray = objectMapper.readTree(oldKeyDataJson);

			// Create a result list with a single element
			List<Map<String, List<Map<String, Object>>>> result = new ArrayList<>();

			// Create a single element to hold the transformed data
			Map<String, List<Map<String, Object>>> transformedDataMap = new LinkedHashMap<>();

			// Iterate through updatedKeyJson keys (e.g., "Customer", "Site")
			Iterator<String> updatedKeys = updatedKeyJsonObject.fieldNames();
			while (updatedKeys.hasNext()) {
				String key = updatedKeys.next();
				JsonNode updatedKeyMappings = updatedKeyJsonObject.get(key);

				// Create a mapping of headerName to value from updatedKeyJson
				Map<String, String> keyMapping = new LinkedHashMap<>();
				for (JsonNode mapping : updatedKeyMappings) {
					String headerName = mapping.get("headerName").asText();
					String value = mapping.get("value").asText();
					keyMapping.put(headerName, value);
				}

				// Find the corresponding data array in oldKeyDataJsonArray
				for (JsonNode oldDataArray : oldKeyDataJsonArray) {
					if (oldDataArray.has(key)) {
						JsonNode dataArray = oldDataArray.get(key);
						List<Map<String, Object>> transformedData = new ArrayList<>();

						// Iterate through oldKeyDataJson for the given key
						for (JsonNode item : dataArray) {
							Map<String, Object> transformedItem = new LinkedHashMap<>();
							Iterator<Map.Entry<String, JsonNode>> fields = item.fields();

							// Map old keys to new keys using keyMapping
							while (fields.hasNext()) {
								Map.Entry<String, JsonNode> field = fields.next();
								String oldKey = field.getKey();
								String newKey = keyMapping.get(oldKey);
								JsonNode valueNode = field.getValue();
								Object newValue = valueNode.isTextual() && ("TRUE".equalsIgnoreCase(valueNode.asText())
										|| "FALSE".equalsIgnoreCase(valueNode.asText()))
												? Boolean.parseBoolean(valueNode.asText())
												: valueNode.asText();
								transformedItem.put(newKey, newValue);
							}

							transformedData.add(transformedItem);
						}

						// Add the transformed data to the single element
						transformedDataMap.put(key, transformedData);
					}
				}
			}

			// Add the single element to the result list
			result.add(transformedDataMap);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/////////// VERY IMPORTANT NOT DELETE THE BELOW///////////

//	@PostMapping("/DownloadExcel/{id}")
//	public ResponseEntity<?> importdatadownloadexcel(@PathVariable Long id,
//			@RequestBody Map<String, List<Map<String, Object>>> jsonData) {
//
//		TemplateFileUpload templateFileUpload = fileUploadService.getTemplatebyid(id);
//
//		String entity_name = templateFileUpload.getEntity_name();
//
//		BulkUpload_t bulkUpload_t = bulkUpload_Repository.getentityName(entity_name);
//		String ruleLine = bulkUpload_t.getRule_line();
//
//		JsonParser parser = new JsonParser();
//		JsonElement element = parser.parse(ruleLine);
//		JsonArray array = element.getAsJsonArray();
//		Iterator<JsonElement> iterator = array.iterator();
//
//		String fromsheet = null;
//		String fromColumn = null;
//		String validationTable = null;
//		String checkColumn = null;
//		String useColumn = null;
//		String replacementtable = null;
//		String replacementcolumn = null;
//
//		while (iterator.hasNext()) {
//			JsonElement next = iterator.next();
//			JsonObject jsonObject = next.getAsJsonObject();
//
//			fromsheet = jsonObject.get("fromsheet").getAsString();
//			fromColumn = jsonObject.get("fromColumn").getAsString();
//			validationTable = jsonObject.get("validationTable").getAsString();
//			checkColumn = jsonObject.get("checkColumn").getAsString();
//			useColumn = jsonObject.get("useColumn").getAsString();
//			replacementtable = jsonObject.get("useTable").getAsString();
//			replacementcolumn = jsonObject.get("replacementcolumn").getAsString();
//
//			break;
//		}
//
//		String customerTableName = validationTable;
//		String siteTableName = replacementtable;
//
//		try {
//			Set<String> tableNames = jsonData.keySet(); // Get all unique table names
//
//			String siteEntityName = null;
//			for (String tableName : tableNames) {
//				List<Map<String, Object>> tableData = jsonData.get(tableName);
//
//				// Process tableData based on the tableName (e.g., "Site" or "Customer")
//				System.out.println("Table Name: " + tableName);
//				for (Map<String, Object> row : tableData) {
//					// Process individual rows within the table data
//					System.out.println("Row Data: " + row);
//				}
//			}
//
//			List<Map<String, Object>> processedDataList = new ArrayList<>(); // List to hold processed data rows
//
//			// Iterate through each customer data entry
//			List<Map<String, Object>> customerData = jsonData.get("Customer");
//			for (Map<String, Object> insertCustomerData : customerData) {
//				String customerName = (String) insertCustomerData.get(checkColumn);
//
//				// Check if the customerName is not null and iterate through "Site" data
//				List<Map<String, Object>> siteData = jsonData.get(fromsheet);
//
//				List<Map<String, Object>> matchedSiteData = new ArrayList<>();
//				if (customerName != null) {
//					// Iterate through "Site" data and check for a matching "entity_name"
//					for (Map<String, Object> siteRow : siteData) {
//						// Specify the index as "AM" (39th column)
////	                    String columnIndex = "AM";
//						String columnIndex = fromColumn;
//
//						// Retrieve the value at the specified index
//
//						for (Map.Entry<String, Object> entry : siteRow.entrySet()) {
//							if (entry.getKey().equals(columnIndex)) {
//								siteEntityName = (String) entry.getValue();
//								break; // Exit the loop once the value is found
//							}
//						}
//
//						if (customerName.equals(siteEntityName)) {
//							// Add the matching "Site" data to the list
//							matchedSiteData.add(siteRow);
//						}
//					}
//				}
//
//				ObjectMapper objectMapper = new ObjectMapper();
//				String insertCustomerDataJson = objectMapper.writeValueAsString(insertCustomerData);
//				String matchedSiteDataJson = objectMapper.writeValueAsString(matchedSiteData);
//
//				String customerSql = getInsertQuery(customerTableName, insertCustomerData);
//
//				// Insert data into the customer table
//				Object[] customerValues = new Object[insertCustomerData.size() + 5];
//				int index = 0;
//				for (Object value : insertCustomerData.values()) {
//					customerValues[index++] = value;
//				}
//				customerValues[index++] = new Timestamp(System.currentTimeMillis()); // created_at
//				customerValues[index++] = null; // created_by
//				customerValues[index++] = null; // updated_by
//				customerValues[index++] = new Timestamp(System.currentTimeMillis()); // updated_at
//				customerValues[index] = null; // account_id
//				jdbcTemplate.update(customerSql, customerValues);
//
//				// we can use useColumn here
//				Long generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
//
//				List<Map<String, Object>> insertMatchedSiteData = new ArrayList<>(); // List to hold processed data rows
//
//				for (Map<String, Object> siteRow : matchedSiteData) {
//					// Replace "Customer Name" with "customer_master_id" if it exists
//					if (siteRow.containsKey(siteEntityName)) {
//						siteRow.put(replacementcolumn, generatedId);
//						siteRow.remove(siteEntityName);
//					}
//					insertMatchedSiteData.add(siteRow);
//				}
//
//				for (Map<String, Object> siteRow : insertMatchedSiteData) {
//					Object[] siteValues = new Object[siteRow.size() + 5]; // Create a new array for each row
//
//					int siteIndex = 0;
//					for (Object value : siteRow.values()) {
//						siteValues[siteIndex++] = value;
//					}
//
//					siteValues[siteIndex++] = new Timestamp(System.currentTimeMillis()); // created_at
//					siteValues[siteIndex++] = null; // created_by
//					siteValues[siteIndex++] = null; // updated_by
//					siteValues[siteIndex++] = new Timestamp(System.currentTimeMillis()); // updated_at
//					siteValues[siteIndex] = null; // account_id
//
//					String siteSql = getInsertQuery(siteTableName, siteRow);
//					jdbcTemplate.update(siteSql, siteValues);
//				}
//
//				// Add the processed customer data to the list
//				processedDataList.add(insertCustomerData);
//			}
//
//			// Use a LinkedHashMap to preserve insertion order in the response
//			Map<String, List<Map<String, Object>>> response = new LinkedHashMap<>();
//			response.put("result", processedDataList);
//
//			return new ResponseEntity<>(response, HttpStatus.OK);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			// Handle exceptions and return an appropriate response
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@PostMapping("/DownloadExcel/{id}")
	public ResponseEntity<?> importdatadownloadexcel( @PathVariable Long id,
			@RequestParam(name = "jsonData") String jsonDataParam,
			@RequestParam(name = "ruleData") String ruleDataParam)
			throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {

		

		// Inside your method
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode ruleDataNode = objectMapper.readTree(ruleDataParam);

		if (ruleDataNode.isArray() && ruleDataNode.isEmpty()) {
		    // If ruleDataParam is an empty JSON array, directly upload data to the table
		    return uploadDataToTable(id, jsonDataParam);
		}
		
	

		// Parse jsonDataParam to extract the JSON data
		Map<String, List<Map<String, Object>>> jsonData = objectMapper.readValue(
				URLDecoder.decode(jsonDataParam, "UTF-8"), new TypeReference<Map<String, List<Map<String, Object>>>>() {
				});

		// Parse ruleDataParam to extract the JSON data
		List<Map<String, String>> ruleData = objectMapper.readValue(URLDecoder.decode(ruleDataParam, "UTF-8"),
				new TypeReference<List<Map<String, String>>>() {
				});

		String fromsheet = null;
		String fromColumn = null;
		String validationTable = null;
		String checkColumn = null;
		String useColumn = null;
		String replacementtable = null;
		String replacementcolumn = null;

		
		
		if (ruleData != null && !ruleData.isEmpty()) {
			// Assuming you want to access the first element in the array
			Map<String, String> rule = ruleData.get(0);

			fromsheet = rule.get("fromsheet");
			fromColumn = rule.get("fromColumn");
			validationTable = rule.get("validationTable");
			checkColumn = rule.get("checkColumn");
			useColumn = rule.get("useColumn");
			replacementtable = rule.get("replacementTable");
			replacementcolumn = rule.get("replacementcolumn");
		}

		String customerTableName = validationTable;
		String siteTableName = replacementtable;

		try {

			String siteEntityName = null;
			String responseMessage = null;
			// Create a workbook
			XSSFWorkbook workbook = new XSSFWorkbook();

			// Create a sheet for customer data
			XSSFSheet customerSheet = workbook.createSheet("Customer");

			// Create a sheet for site data
			XSSFSheet siteSheet = workbook.createSheet("Site");
			

			List<Map<String, Object>> customerDataList = new ArrayList<>();
			List<Map<String, Object>> siteDataList = new ArrayList<>();

			// Get the column names for customer data
			List<String> customerColumnNames = getColumnNames(jsonData.get("Customer").get(0));
			// Create the header row for customer data
			XSSFRow customerHeaderRow = customerSheet.createRow(0);
			for (int i = 0; i < customerColumnNames.size(); i++) {
				XSSFCell cell = customerHeaderRow.createCell(i);
				cell.setCellValue(customerColumnNames.get(i));
			}

			// Get the column names for site data
			List<String> siteColumnNames = getColumnNames(jsonData.get(fromsheet).get(0));
			// Create the header row for site data
			XSSFRow siteHeaderRow = siteSheet.createRow(0);
			for (int i = 0; i < siteColumnNames.size(); i++) {
				XSSFCell cell = siteHeaderRow.createCell(i);
				cell.setCellValue(siteColumnNames.get(i));
			}

			// Iterate through each customer data entry
			List<Map<String, Object>> customerData = jsonData.get("Customer");
			for (Map<String, Object> insertCustomerData : customerData) {
				String customerName = (String) insertCustomerData.get(checkColumn);

				Map<String, Object> processedRow = new HashMap<>();

				// Check if the customerName is not null and iterate through "Site" data
				List<Map<String, Object>> siteData = jsonData.get(fromsheet);

				List<Map<String, Object>> matchedSiteData = new ArrayList<>();
				if (customerName != null) {
					// Iterate through "Site" data and check for a matching "entity_name"
					for (Map<String, Object> siteRow : siteData) {
						String columnIndex = fromColumn;

						String cellValue = getCellValue(jsonData, fromsheet, columnIndex);
						responseMessage = cellValue;

						for (Map.Entry<String, Object> entry : siteRow.entrySet()) {
							if (entry.getKey().equals(responseMessage)) {
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

				String insertCustomerDataJson = objectMapper.writeValueAsString(insertCustomerData);
				String matchedSiteDataJson = objectMapper.writeValueAsString(matchedSiteData);

				String customerSql = getInsertQuery(customerTableName, insertCustomerData);

				// Insert data into the customer table
				try {
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
					processedRow.putAll(insertCustomerData);
					processedRow.put("Status", "Success");
					processedRow.put("Exception", "NA");
					customerDataList.add(processedRow);
				} catch (DataIntegrityViolationException e) {
					processedRow.putAll(insertCustomerData);
					processedRow.put("Status", "Error");
					String exceptionMessage = extractExceptionMessage(e);
					processedRow.put("Exception", exceptionMessage);
					e.printStackTrace();
					customerDataList.add(processedRow);
				}

				// Get the generated ID from the database
				Long generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

				List<Map<String, Object>> newMatchedSiteData = new ArrayList<>();

				for (Map<String, Object> siteRow : matchedSiteData) {
					// Replace "Customer Name" with "customer_master_id" if it exists
					if (siteRow.containsKey(responseMessage)) {
						siteRow.put(replacementcolumn, generatedId);
						// siteRow.remove(responseMessage);
					//	siteRow.remove(responseMessage);
					}
					newMatchedSiteData.add(siteRow);
				}

				// Insert site data for this customer
				for (Map<String, Object> siteRow : newMatchedSiteData) {
					Map<String, Object> siteProcessedRow = new HashMap<>();
					try {
						Object[] siteValues = new Object[siteRow.size() + 5];

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
						siteProcessedRow.putAll(siteRow);
						siteProcessedRow.put("Status", "Success");
						siteProcessedRow.put("Exception", "NA");
						siteDataList.add(siteProcessedRow);
					} catch (DataIntegrityViolationException ex) {
						siteProcessedRow.putAll(siteRow);
						siteProcessedRow.put("Status", "Error");
						String exceptionMessage = extractExceptionMessage(ex);
						siteProcessedRow.put("Exception", exceptionMessage);
						ex.printStackTrace();
						siteDataList.add(siteProcessedRow);
					}
				}
			}

			// Write customer data to the customer sheet
			writeDataToSheet(customerDataList, customerSheet, customerColumnNames);
			// Write site data to the site sheet
			writeDataToSheet(siteDataList, siteSheet, siteColumnNames);

			String filepath = "import-data";

			long unixTimestamp = System.currentTimeMillis();

			// Create the file name with the Unix timestamp
			String excelFile = "failed_records_" + unixTimestamp + ".xlsx";

			// String excelFilePath = "D:\\REAL IT
			// SOLUTION\\SureSetuOld\\suresetu-mohasin205\\backend\\import-data\\processingfile\\failed_records.xlsx";

			String location = projectPath + File.separator + filepath;
			File dir2 = new File(location);
			if (!dir2.exists()) {
				dir2.mkdir();
			}

//			String file_name2 = file.getOriginalFilename();

			File staticdir2 = new File(location + "/statusFile");
			if (!staticdir2.exists()) {
				staticdir2.mkdir();
			}

			String excelFilePath = staticdir2 + File.separator + excelFile;

			Optional<TemplateFileUpload> fileUploadOptional = temprepo.findById(id);

			if (fileUploadOptional.isPresent()) {
				TemplateFileUpload fileUpload = fileUploadOptional.get();
				fileUpload.setDownloadfileLocation(excelFilePath);
				temprepo.save(fileUpload);
			}

			try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
				workbook.write(fileOutputStream);
			} catch (IOException e) {
				e.printStackTrace();
				// Handle the exception appropriately
			}

			return ResponseEntity.ok().body("Excel file saved successfully to: " + excelFilePath);

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

	
	
	private ResponseEntity<?> uploadDataToTable(Long id, String jsonDataParam) {
	    try {
	        // Get table name from TemplateFileUpload entity
	        Optional<TemplateFileUpload> fileUploadOptional = temprepo.findById(id);
	        if (fileUploadOptional.isPresent()) {
	            TemplateFileUpload fileUpload = fileUploadOptional.get();
//	            String tableName = fileUpload.getTablename();
	            String tableName = "aa";
	            // Insert data into the specified table
	            insertDataIntoTable(tableName, jsonDataParam);
	            return ResponseEntity.ok().body("Data uploaded successfully to table: " + tableName);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template file with id " + id + " not found");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exceptions and return an appropriate response
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	private void insertDataIntoTable(String tableName, String jsonDataParam) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        // Parse jsonDataParam to extract the JSON data
	        Map<String, List<Map<String, Object>>> jsonData = objectMapper.readValue(
	                URLDecoder.decode(jsonDataParam, "UTF-8"), new TypeReference<Map<String, List<Map<String, Object>>>>() {});

	        // Iterate over the entries of the jsonData map
	        for (Map.Entry<String, List<Map<String, Object>>> entry : jsonData.entrySet()) {
	            String sheetName = entry.getKey(); // Get the sheet name dynamically
	            List<Map<String, Object>> data = entry.getValue(); // Get the list of maps dynamically
	            
	            // Now you have the list of maps for the current sheet, you can iterate through it and perform your database insertion logic
	            for (Map<String, Object> rowData : data) {
	                StringBuilder columns = new StringBuilder();
	                StringBuilder values = new StringBuilder();
	                List<Object> params = new ArrayList<>();

	                // Build column names and values dynamically
	                for (Map.Entry<String, Object> columnEntry : rowData.entrySet()) {
	                    if (columns.length() > 0) {
	                        columns.append(", ");
	                        values.append(", ");
	                    }
	                    columns.append(columnEntry.getKey());
	                    values.append("?");
	                    params.add(columnEntry.getValue());
	                }

	                // Construct the SQL query
	                String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";

	                // Execute the SQL query
	                jdbcTemplate.update(sql, params.toArray());
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exceptions appropriately
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
	
	
	//get table column

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


	
	
	
	private String getCellValue(Map<String, List<Map<String, Object>>> jsonData, String sheetName,
	        String cellAddress) throws IOException {
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


	        if (columnIndex >= 0 ) {
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
	
	
	
	
	
//	@PostMapping("/DownloadExcel/{entityName}")
//	public ResponseEntity<Resource> importdatadownloadexcel(@PathVariable String entityName,
//	        @RequestBody List<Map<String, Object>> data) throws IOException {
//	    List<Map<String, Object>> processedData = processData(entityName, data);
//	    if (!processedData.isEmpty()) {
//	        Workbook workbook = createWorkbook(processedData);
//	        Resource resource = createExcelResource(workbook);
//	        return ResponseEntity.ok()
//	                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=failed_records.xlsx")
//	                .body(resource);
//	    }
//	    return ResponseEntity.ok().body(null);
//	}

	
	
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



}
