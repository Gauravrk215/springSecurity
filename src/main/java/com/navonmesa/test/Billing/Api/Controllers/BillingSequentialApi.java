//package com.realnet.Billing.Api.Controllers;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map.Entry;
//import java.util.Optional;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
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
//
//@RestController
//
//public class BillingSequentialApi {
//
//	@Autowired
//	private ServiceOrder_Repository serviceOrder_Repository;
//
//	@Autowired
//	private DocumentBuilder_Service Service;
//
////	@Autowired
////	private Fileupload_helper fileuploadhelper;
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
//	@Autowired
//	private ServiceOrder_Service serviceOrder_Service;
//
//	@GetMapping("/process-all-customers")
//	public ResponseEntity<String> processAllCustomers() {
//		try {
//			// Step 1: Create Service Orders for all customers
//			List<Customer_master_t> customers = customer_master_Repository.findAll();
//			for (Customer_master_t customer : customers) {
//				serviceOrder_Service.createServiceOrderFromCustomer(customer);
//
//			}
//
//			// Step 2: Approve all pending service orders
//			int updatedCount = serviceOrder_Service.approvePendingOrders();
//
//			// step 3:
//
//			List<Long> autoApprovedServiceOrderIds = serviceOrder_Repository.findIdsByStatusOrderByAsc("AutoApproved");
//
//			// Process the service orders
//			for (Long serviceOrderId : autoApprovedServiceOrderIds) {
//				Optional<ServiceOrder_t> serviceOrderOptional = serviceOrder_Repository.findById(serviceOrderId);
//
//				if (serviceOrderOptional.isPresent()) {
//					ServiceOrder_t serviceOrder = serviceOrderOptional.get();
//					String entityName = serviceOrder.getEntity();
//
//					// Fetch the corresponding customer(s) by entity name
//					List<Customer_master_t> customers1 = customer_master_Repository.findByEntityName(entityName);
//
//					if (!customers1.isEmpty()) {
//						// Assuming you want to process each customer found
//						for (Customer_master_t customer : customers1) {
//							// Now you have the serviceOrderId, entityName, and customer for processing
//							String proformaInvoiceFileName = generateProformaInvoice(serviceOrderId);
//
//							// step 4
//							sendEmailWithProformaInvoice(customer, proformaInvoiceFileName);
//						}
//					} else {
//						// Handle the case where no customer was found for the given entity name
//					}
//				} else {
//					// Handle the case where no service order was found for the given ID
//				}
//			}
//
//			return ResponseEntity.ok("Processed all customers and generated invoices. " + updatedCount
//					+ " service orders were approved.");
//		} catch (Exception e) {
//			// Handle exceptions appropriately
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Error processing customers: " + e.getMessage());
//		}
//	}
//
//	private String generateProformaInvoice(Long serviceOrderId) throws Exception {
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
//		// long serviceOrderId = serviceOrderId2.getId();
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
//
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
//		return replacerule;
//
//	}
//
//	private void sendEmailWithProformaInvoice(Customer_master_t customer, String fileName) {
//
//		Long id = customer.getId();
//		String email = customer.getEmail();
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
