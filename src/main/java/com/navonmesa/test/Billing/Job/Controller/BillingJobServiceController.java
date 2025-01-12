//package com.realnet.Billing.Job.Controller;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.realnet.Billing.Job.Entity.BillingJobEntity;
//import com.realnet.Billing.Job.Repository.BillingJobRepository;
//
//import com.realnet.utils.Port_Constant;
//
//@RestController
//@RequestMapping("/token/BillingWorkflow/surejob")
//public class BillingJobServiceController {
//	Logger log = LoggerFactory.getLogger(BillingJobServiceController.class);
//
//	@Autowired
//	private BillingJobRepository jobrepo;
//
//	@Autowired
//	private BillingPerfomaWorkFlowLineRepository flowrepo;
//
//	@Autowired
//	private BillingPerfomaWorkFlowLineRepository line_repo;
//
//	@Autowired
//	private BillingTaxInvoiceWorkFlowLineRepository billingTaxInvoiceWorkFlowLineRepository;
//
//	@Autowired
//	BillingJobService service;
//
//	@PostMapping("/assignJob")
//	public ResponseEntity<?> jobAssign() {
//		BillingJobEntity obj = new BillingJobEntity();
//		BillingJobEntity obj2 = new BillingJobEntity();
//		// DataflowJobConsolidationEntity obj3 = new DataflowJobConsolidationEntity();
//
//		obj.setConnection_name(null);
//		obj.setJob_type("PerfomaInvoice");
//		obj.setMethod("GET");
//		obj.setParameters(null);
//		obj.setUrl("/token/PerfomaSequentialApi/executeWorkflow");
//		obj.setRef(null);
//		jobrepo.save(obj);
//
//		obj2.setConnection_name(null);
//		obj2.setJob_type("TaxInvoice");
//		obj2.setMethod("GET");
//		obj2.setParameters(null);
//		obj2.setRef(null);
//		obj2.setUrl("/token/consolidation/MergeData");
//		jobrepo.save(obj2);
//
//		return new ResponseEntity<>(obj2, HttpStatus.ACCEPTED);
//	}
//
//	@GetMapping("/getjob/{job_type}")
//	public ResponseEntity<?> getByJob(@PathVariable String job_type) {
//
//		BillingJobEntity jobtype = jobrepo.getByJobType(job_type);
//		return new ResponseEntity<>(jobtype, HttpStatus.ACCEPTED);
//	}
//
//	@GetMapping("/create_job/{id}")
//	public ResponseEntity<?> createjob(@PathVariable Long id) throws SQLException {
//
//		String job_url = "";
//		String CRON_exp = "";
//		String job_type = "";
//		String title = "";
//		String node = "";
//		ArrayList<Object> list = new ArrayList<>();
//		BillingPerfomaWorkFlowLine lines = flowrepo.getSetuWorkflowlines(id);
//		String str = lines.getModel();
//
//		JsonParser parser = new JsonParser();
//		JsonElement element = parser.parse(str);
//		JsonArray array = element.getAsJsonArray();
////		Iterator iterator = array.iterator();
//
//		List<String> jobNamesList = new ArrayList<>();
//
//		for (JsonElement jsonElement : array) {
//
//			JsonObject jsonObject = jsonElement.getAsJsonObject();
//
//			title = jsonObject.get("title").getAsString();
//
//			StringBuilder builder = new StringBuilder();
//
//			builder.append("no data");
//			System.out.println(builder.toString());
//
//			Map<String, String> jobprodata = new HashMap<String, String>();
//			jobprodata.put("jobName", title + "_" + System.currentTimeMillis());
//			jobprodata.put("jobGroup", "PerfomaInvoice");
//
//			jobprodata.put("startTime", "2022-12-26T13:02");
//			jobprodata.put("counter", "5");
//			jobprodata.put("repeatTime", "5");
//			jobprodata.put("cronExpression", "0/10 * * * * ?");
//			jobprodata.put("line_id", id.toString());
//			jobprodata.put("node_id", "null");
//
//			System.out.println(jobprodata);
//
//			System.out.println(jobprodata);
//
//			RestTemplate restTemplate = new RestTemplate();
//			String jobprourl2 = "http://" + Port_Constant.LOCAL_HOST + ":" + Port_Constant.SURE_JOB_8089
//					+ "/surejob/schedule";
//			HttpHeaders headers2 = getHeaders();
//			HttpEntity<Object> request2 = new HttpEntity<Object>(jobprodata, headers2);
//
//			ResponseEntity<Object> res2 = restTemplate.postForEntity(jobprourl2, request2, Object.class);
//			System.out.println(res2.getStatusCodeValue());
//
//			if (res2.getStatusCodeValue() == 200) {
//				log.info("Gitea data inserted in sure job");
//				System.out.println(res2.getBody());
//				jobNamesList.add(jobprodata.get("jobName"));
//				// jobNamesList.add(jobprodata.get("jobGroup"));
//
//			}
//		}
//
//		return new ResponseEntity<>(jobNamesList, HttpStatus.ACCEPTED);
//	}
//
//	@GetMapping("/surejob/{id}")
//	public ResponseEntity<?> forjobscheduler(@PathVariable Long id) throws SQLException {
//
//		ArrayList<Object> list = new ArrayList<>();
//		BillingPerfomaWorkFlowLine lines = flowrepo.getSetuWorkflowlines(id);
//		String str = lines.getModel();
//
//		JsonParser parser = new JsonParser();
//		JsonElement element = parser.parse(str);
//		JsonArray array = element.getAsJsonArray();
//		Iterator iterator = array.iterator();
//
//		String job_url = "";
//		String CRON_exp = "";
//		String job_type = "";
//		BillingJobEntity job = null;
//
//		Boolean auto_mapping = true;
//		while (iterator.hasNext()) {
//
//			Object next = iterator.next();
//			JsonElement parse = parser.parse(next.toString());
//			JsonObject jsonObject = parse.getAsJsonObject();
//			// int i = jsonObject.get("id").getAsInt();
//
//
//			job_type = "PerfomaInvoice";
//
////			if (job_type.isEmpty() || job_type.contains("null") || job_type == null) {
////				job_type = jsonObject.get("type").toString().replaceAll("\"", "");
////			}
//
//			job = jobrepo.getByJobType(job_type);
//
//			break;
////			}
//
//		}
//		return new ResponseEntity<>(job, HttpStatus.ACCEPTED);
//	}
////	GET DATA FLOW LINE
//
//	@GetMapping("/getline/{id}")
//	public ResponseEntity<?> getline(@PathVariable Long id) {
//
//		return new ResponseEntity<>(line_repo.getSetuWorkflowlines(id), HttpStatus.ACCEPTED);
//
//	}
//
//	private HttpHeaders getHeaders() {
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//		return headers;
//	}
//
//	// Tax Invoice Job Start
//
//	@GetMapping("/create_job/{id}")
//	public ResponseEntity<?> createjobforTaxInvoice(@PathVariable Long id) throws SQLException {
//
//		String job_url = "";
//		String CRON_exp = "";
//		String job_type = "";
//		String title = "";
//		String node = "";
//		ArrayList<Object> list = new ArrayList<>();
//		BillingTaxInvoiceWorkFlowLine lines = billingTaxInvoiceWorkFlowLineRepository.getSetuWorkflowlines(id);
//		String str = lines.getModel();
//
//		JsonParser parser = new JsonParser();
//		JsonElement element = parser.parse(str);
//		JsonArray array = element.getAsJsonArray();
////		Iterator iterator = array.iterator();
//
//		List<String> jobNamesList = new ArrayList<>();
//
//		for (JsonElement jsonElement : array) {
//
//			JsonObject jsonObject = jsonElement.getAsJsonObject();
//
//			title = jsonObject.get("title").getAsString();
//
//			StringBuilder builder = new StringBuilder();
//
//			builder.append("no data");
//			System.out.println(builder.toString());
//
//			Map<String, String> jobprodata = new HashMap<String, String>();
//			jobprodata.put("jobName", title + "_" + System.currentTimeMillis());
//			jobprodata.put("jobGroup", "TaxInvoice");
//
//			jobprodata.put("startTime", "2022-12-26T13:02");
//			jobprodata.put("counter", "5");
//			jobprodata.put("repeatTime", "5");
//			jobprodata.put("cronExpression", "0/10 * * * * ?");
//			jobprodata.put("line_id", id.toString());
//			jobprodata.put("node_id", "null");
//
//			System.out.println(jobprodata);
//
//			System.out.println(jobprodata);
//
//			RestTemplate restTemplate = new RestTemplate();
//			String jobprourl2 = "http://" + Port_Constant.LOCAL_HOST + ":" + Port_Constant.SURE_JOB_8089
//					+ "/surejob/schedule";
//			HttpHeaders headers2 = getHeaders();
//			HttpEntity<Object> request2 = new HttpEntity<Object>(jobprodata, headers2);
//
//			ResponseEntity<Object> res2 = restTemplate.postForEntity(jobprourl2, request2, Object.class);
//			System.out.println(res2.getStatusCodeValue());
//
//			if (res2.getStatusCodeValue() == 200) {
//				log.info("Gitea data inserted in sure job");
//				System.out.println(res2.getBody());
//				jobNamesList.add(jobprodata.get("jobName"));
//				// jobNamesList.add(jobprodata.get("jobGroup"));
//
//			}
//		}
//
//		return new ResponseEntity<>(jobNamesList, HttpStatus.ACCEPTED);
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	@GetMapping("/surejob/taxInvoice/{id}")
//	public ResponseEntity<?> forjobschedulerInvoicetax(@PathVariable Long id) throws SQLException {
//
//		ArrayList<Object> list = new ArrayList<>();
//		BillingTaxInvoiceWorkFlowLine lines = billingTaxInvoiceWorkFlowLineRepository.getSetuWorkflowlines(id);
//		String str = lines.getModel();
//
//		JsonParser parser = new JsonParser();
//		JsonElement element = parser.parse(str);
//		JsonArray array = element.getAsJsonArray();
//		Iterator iterator = array.iterator();
//
//		String job_url = "";
//		String CRON_exp = "";
//		String job_type = "";
//		BillingJobEntity job = null;
//
//		Boolean auto_mapping = true;
//		while (iterator.hasNext()) {
//
//			Object next = iterator.next();
//			JsonElement parse = parser.parse(next.toString());
//			JsonObject jsonObject = parse.getAsJsonObject();
//			// int i = jsonObject.get("id").getAsInt();
//
//
//			job_type = "TaxInvoice";
//
////			if (job_type.isEmpty() || job_type.contains("null") || job_type == null) {
////				job_type = jsonObject.get("type").toString().replaceAll("\"", "");
////			}
//
//			job = jobrepo.getByJobType(job_type);
//
//			break;
////			}
//
//		}
//		return new ResponseEntity<>(job, HttpStatus.ACCEPTED);
//	}
//
//}
