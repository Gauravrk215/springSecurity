//package com.realnet.Billing.Api.Controllers;
//
//import java.io.ByteArrayOutputStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map.Entry;
//import java.util.Optional;
//import java.util.Set;
//
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.realnet.Billing.Entitys.Invoice_t;
//import com.realnet.Billing.Entitys.ServiceOrder_t;
//import com.realnet.Billing.Repositorys.ServiceOrder_Repository;
//import com.realnet.Billing.Services.ServiceOrder_Service;
//import com.realnet.Communication.Models.Com_jobTable;
//import com.realnet.Communication.Repos.JobTablerepo;
//import com.realnet.Customer_master.Entity.Customer_master_t;
//import com.realnet.Customer_master.Repository.Customer_master_Repository;
//import com.realnet.DocumentBuilder.Entity.DocumentBuilder_t;
//import com.realnet.DocumentBuilder.Entity.Document_builder_lines;
//import com.realnet.DocumentBuilder.Services.DocumentBuilder_Service;
//import com.realnet.DocumentBuilder.Services.StringReplacementService;
//import com.realnet.fileupload.helper.Fileupload_helper;
//
//@RestController
//@RequestMapping("/api/serviceorders")
//public class ServiceOrderApiController {
//
//	@Autowired
//	private ServiceOrder_Service serviceOrder_Service;
//
//	@Autowired
//	private ServiceOrder_Repository serviceOrder_Repository;
//
//	@Autowired
//	private DocumentBuilder_Service Service;
//
//	@Autowired
//	private Fileupload_helper fileuploadhelper;
//
//	@Autowired
//	private StringReplacementService replacementService;
//
//	@Autowired
//	private Customer_master_Repository customer_master_Repository;
//
//	@Autowired
//	private JobTablerepo Com_jobTablerepo;
//
//	@Value("${projectPath}")
//	private String projectPath;
//
//	public final String UPLOAD_DIREC = "/Files";
//
//	// based on the period autocomplete all pending status
//	@GetMapping("/update-status")
//	public ResponseEntity<String> updateStatusForExpiredOrders() {
//		int updatedCount = serviceOrder_Service.updateStatusForExpiredOrders();
//		return ResponseEntity.ok("Updated " + updatedCount + " orders");
//	}
//
//	// download profoma invoice
//	@GetMapping("/generate-excel/{id}")
//	public ResponseEntity<byte[]> generateExcel(@PathVariable Long id) {
//		ServiceOrder_t serviceOrder = serviceOrder_Service.findById(id);
//
//		if (serviceOrder != null) {
//			Workbook workbook = generateExcelForServiceOrder(serviceOrder);
//
//			try {
//				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//				workbook.write(outputStream);
//				byte[] excelBytes = outputStream.toByteArray();
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//				headers.setContentDispositionFormData("attachment", "service_order.xlsx");
//				return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
//			} catch (Exception e) {
//				// Handle exceptions
//			}
//		}
//
//		return ResponseEntity.notFound().build();
//	}
//
//	public Workbook generateExcelForServiceOrder(ServiceOrder_t serviceOrder) {
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Service Order");
//
//		// Create header row
//		Row headerRow = sheet.createRow(0);
//		headerRow.createCell(0).setCellValue("Service Order ID");
//		headerRow.createCell(1).setCellValue("Service Order Seq");
//
//		// Add more header cells for other fields as needed...
//
//		// Create data row
//		Row dataRow = sheet.createRow(1);
//		dataRow.createCell(0).setCellValue(serviceOrder.getId());
//		dataRow.createCell(1).setCellValue(serviceOrder.getServiceOrderSeq());
//		// Add more data cells for other fields as needed...
//
//		return workbook;
//	}
//
//	// to HOLD status
////	@PutMapping("/change-service-order-status/{profomaInvoiceId}")
////	public ResponseEntity<String> changeServiceOrderStatus(@PathVariable Long profomaInvoiceId) {
////		// Fetch the ProfomaInvoice by ID
////		ProfomaInvoice profomaInvoice = profomaInvoice_Service.findById(profomaInvoiceId);
////
////		if (profomaInvoice != null) {
////			// Get the associated ServiceOrderId from ProfomaInvoice
////			Long serviceOrderId = profomaInvoice.getServiceOrderId();
////
////			// Fetch the associated ServiceOrder_t entity
////			ServiceOrder_t serviceOrder = serviceOrder_Service.findById(serviceOrderId);
////
////			if (serviceOrder != null) {
////				// Update the status to "HOLD"
////				serviceOrder.setStatus("HOLD");
////				serviceOrder_Service.saveData(serviceOrder);
////
////				return ResponseEntity.ok("ServiceOrder status updated to HOLD");
////			}
////		}
////
////		return ResponseEntity.notFound().build();
////	}
//
//	// Auto Creation Service order to invoicecopyServiceOrderToInvoice api
//	@GetMapping("/copyServiceOrderToInvoice")
//	public List<Invoice_t> copyServiceOrderToInvoice() {
//		List<Invoice_t> createdInvoice = serviceOrder_Service.createInvoicesForAutoApprovedServiceOrders();
//		return createdInvoice;
//	}
//
////document status HOLD api   (custmer hold) (old history table crude insert record),,service order release then it release hold		    
//	@PutMapping("/updateHoldstatus/{id}")
//	public ResponseEntity<String> updateStatusById(@PathVariable Long id) {
//		try {
//			// Hardcode the new status value to "HOLD"
//			serviceOrder_Service.updateStatusById(id, "HOLD");
//			return ResponseEntity.ok("Status updated to HOLD successfully");
//		} catch (Exception e) {
//			// Handle exceptions, e.g., if the entity with the given ID is not found
//			return ResponseEntity.badRequest().body("Failed to update status: " + e.getMessage());
//		}
//	}
//
////so release hold		    
//	@PutMapping("/updateResolvestatus/{id}")
//	public ResponseEntity<String> updateStatusResolvedById(@PathVariable Long id) {
//		try {
//			
//			serviceOrder_Service.updateStatusById(id, "RESOLVED");
//			return ResponseEntity.ok("Status updated to HOLD successfully");
//		} catch (Exception e) {
//			// Handle exceptions, e.g., if the entity with the given ID is not found
//			return ResponseEntity.badRequest().body("Failed to update status: " + e.getMessage());
//		}
//	}
//	
//	@PutMapping("/updateCustomerApprovedestatus/{id}")
//	public ResponseEntity<String> updateStatusCustomerApprovedById(@PathVariable Long id) {
//		try {
//			
//			serviceOrder_Service.updateStatusById(id, "CustomerApproved");
//			return ResponseEntity.ok("Status updated to Approvec successfully");
//		} catch (Exception e) {
//			// Handle exceptions, e.g., if the entity with the given ID is not found
//			return ResponseEntity.badRequest().body("Failed to update status: " + e.getMessage());
//		}
//	}
//
//	@GetMapping("/customerGenerate")
//	public void generateServiceOrder() {
//		serviceOrder_Service.generateServiceOrders();
//	}
//
//	@GetMapping("/approvePending")
//	public ResponseEntity<String> approvePendingOrders() {
//		int updatedCount = serviceOrder_Service.approvePendingOrders();
//		return ResponseEntity.ok("Approved " + updatedCount + " pending orders");
//	}
//
//	@GetMapping("/perfomaInvoiceCreation/{serviceOrderId}")
//	public ResponseEntity<?> perfomaInvoiceCreation(@PathVariable long serviceOrderId) throws Exception {
//
//		long documentId = 98;
//		HashMap<String, String> map = new HashMap<>();
//		JsonParser parser = new JsonParser();
//		DocumentBuilder_t doc = Service.getdetailsbyId(documentId);
//		String file_name = doc.getFile_name();
//		String file_path = doc.getFile_path();
//
//		String replacerule = "";
//		String operation = null;
//		String replaceWith = null;
//		String startstring = null;
//		String endstring = null;
//		String keyword = null;
//		String linestring = null;
//		String cellAddress = null;
//		JsonObject getbodyobject = null;
//		String ModifyfileName = null;
//
//		// .....................//
//
//		List<Document_builder_lines> docline = doc.getDocument_builder_lines();
//
//		for (Document_builder_lines line : docline) {
//			String model = line.getModel();
//			JsonParser parser1 = new JsonParser();
//			JsonArray jsonArray = parser1.parse(model).getAsJsonArray();
//
//			for (JsonElement element : jsonArray) {
//				JsonObject jsonObject = element.getAsJsonObject();
//				if (jsonObject.has("name")) {
//					ModifyfileName = jsonObject.get("name").getAsString();
//					break; // Break the loop once you find the first object with a "name" field
//				}
//			}
//
//			if (ModifyfileName != null) {
//				// Now 'nameValue' contains the value of the "name" field
//				System.out.println("nameValue: " + ModifyfileName);
//				break; // Break the outer loop as well, if needed
//			}
//		}
//
//		if (ModifyfileName == null) {
//			// Handle the case where no object with a "name" field was found
//			System.out.println("No object with a 'name' field found.");
//		}
//
//		ModifyfileName = ModifyfileName.replace("?", String.valueOf(serviceOrderId));
//		ModifyfileName = ModifyfileName + ".docx";
//		String newFilepath = replacementService.copyWordFile(file_path, file_name, file_path, ModifyfileName);
//
//		// ..........................//
//
//		List<Document_builder_lines> lines = doc.getDocument_builder_lines();
//
//		for (Document_builder_lines line : lines) {
//			String model = line.getModel();
//			JsonElement element = parser.parse(model);
//			JsonArray models = element.getAsJsonArray();
//
//			for (JsonElement mod : models) {
//				JsonObject object = mod.getAsJsonObject();
//
//				String type = object.get("type").getAsString();
//				if (type.equalsIgnoreCase("Initialize")) {
//					String a_uri = object.get("a_uri").toString().replaceAll("\"", "");
//					a_uri = a_uri.replace("?", String.valueOf(serviceOrderId));
//					System.out.println(a_uri);
////							Object body = GET(a_uri).getBody();
////							JsonElement getbody = parser.parse(body.toString());
////							getbodyobject = getbody.getAsJsonObject();
//					Object body = GET(a_uri).getBody();
//					ObjectMapper objectMapper = new ObjectMapper();
//					String json = objectMapper.writeValueAsString(body);
//					JsonElement getbody = parser.parse(json);
//					getbodyobject = getbody.getAsJsonObject();
//
//				}
//			}
//
//			for (JsonElement mod : models) {
//				JsonObject object = mod.getAsJsonObject();
//
//				String type = object.get("type").getAsString();
//				if (type.equalsIgnoreCase("Mapper")) {
//					String mapper = object.get("mappers").getAsString();
//
//					JsonElement parse = parser.parse(mapper);
//					JsonArray mapArray = parse.getAsJsonArray();
//					for (JsonElement maps : mapArray) {
//						JsonObject jsonObject = maps.getAsJsonObject();
//						startstring = jsonObject.get("start_string").toString().replaceAll("\"", "");
//						endstring = jsonObject.get("end_string").toString().replaceAll("\"", "");
//						replaceWith = jsonObject.get("replace_with").toString().replaceAll("\"", "");
//						keyword = jsonObject.get("Keyword").toString().replaceAll("\"", "");
//						linestring = jsonObject.get("line_string").toString().replaceAll("\"", "");
//						operation = jsonObject.get("operation").toString().replaceAll("\"", "");
//						cellAddress = jsonObject.get("cellAddress").toString().replaceAll("\"", "");
//						Set<Entry<String, JsonElement>> entrySet = getbodyobject.entrySet();
//						for (Entry<String, JsonElement> entry : entrySet) {
//
//							String key = entry.getKey().toString().replaceAll("\"", "");
//							String value = entry.getValue().toString().replaceAll("\"", "");
//
//							if (replaceWith.equalsIgnoreCase(key)) {
//								replaceWith = value;
//								break;
//							}
//
//						}
//
//						if (operation.contains("replacebyfirstandlast")) {
//
//							replacerule = replacementService.replacewithstartandend(file_path, file_name, startstring,
//									endstring, replaceWith);
//
//						}
//
//						if (operation.contains("appending")) {
//							replacerule = replacementService.appendToSlide(linestring, replaceWith, file_path,
//									file_name);
//						}
//
//						if (operation.contains("linereplacement")) {
//							replacerule = replacementService.linereplacementForPPT(file_path, file_name, keyword,
//									replaceWith);
//						}
//						if (operation.contains("replacement")) {
//							replacerule = replacementService.replacesting(newFilepath, file_name, keyword, replaceWith,
//									ModifyfileName);
//
//						}
//
//						if (operation.contains("excelcellReplace")) {
//							replacerule = replacementService.excelcellReplace(file_path, file_name, cellAddress,
//									replaceWith);
//
//						}
//
//					}
//				}
//
//			}
//		}
//
//		return new ResponseEntity<>(replacerule, HttpStatus.CREATED);
//	}
//
//	@GetMapping("/sendEmail/{id}/{fileName}")
//	public ResponseEntity<String> sendEmail(@PathVariable Long id, @PathVariable String fileName) {
//
//		Optional<Customer_master_t> customer_master_t = customer_master_Repository.findById(id);
//
//		String email = null;
//		if (customer_master_t.isPresent()) {
//			Customer_master_t customer = customer_master_t.get();
//			email = customer.getEmail();
//
//		}
//
//		String gateway = "EMAIL";
//		String sendTo = email;
//		String replacementString = "no";
//		String cc = email;
//		String template = "PerfomaInvoice";
//
//		// Create a new Com_jobTable instance and set its values
//		Com_jobTable comJobTable = new Com_jobTable();
//		comJobTable.setJob_type("EMAIL"); // Set the job type to "EMAIL"
//		comJobTable.setSend_to(sendTo);
//		comJobTable.setGatewayName(gateway);
//		comJobTable.setAttachment(fileName);
//		comJobTable.setGatewaydone("N");
//		comJobTable.setReplacement_string(replacementString);
//		comJobTable.setCc(cc);
//		comJobTable.setReplacement_string(replacementString);
//		comJobTable.setTemplate_name(template);
//		// Save the Com_jobTable entity using your repository or service
//		Com_jobTablerepo.save(comJobTable);
//
//		return new ResponseEntity<>("Email sent successfully and job data saved!", HttpStatus.OK);
//	}
//
//	public ResponseEntity<Object> GET(String get) {
//		RestTemplate restTemplate = new RestTemplate();
//
//		ResponseEntity<Object> u = restTemplate.getForEntity(get, Object.class);
//
//		return u;
//
//	}
//
//}
