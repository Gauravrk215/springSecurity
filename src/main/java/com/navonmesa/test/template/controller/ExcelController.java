//package com.realnet.excel.controller;
package com.realnet.template.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.realnet.template.entity.TemplateFileUpload;
import com.realnet.template.repository.TemplatedataRepo;
import com.realnet.template.service.CSVService;
import com.realnet.template.service.FileUploadService;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;

@RestController
@RequestMapping("api/template")
public class ExcelController {
	@Autowired
	private TemplatedataRepo temprepo;

	@Autowired
	FileUploadService fileupload;
	@Autowired
	private AppUserServiceImpl userService;
//
//	@Autowired
//	private FileUploadService fileUploadService;

//	@Autowired
//	private CommFileuploadhelper fileuploadhelper;

	@GetMapping("/demo/download/{file_type}")
	public ResponseEntity<?> demoTemplate(@PathVariable String file_type) throws IOException {

		if (file_type.equalsIgnoreCase("Customer")) {

			String filename = "Customer" + ".xlsx";
			String[] header = { "Priority Name", "Description", "Is Active", "Effective Start Date",
					"Effective End date" };
			ByteArrayInputStream in = demoTemplate(header, file_type);
			InputStreamResource file = new InputStreamResource(in);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

		} else if (file_type.equalsIgnoreCase("impact")) {

			String filename = "Sr_impact2_t" + ".xlsx";
			String[] header = { "Impact Name", "Description", "Is Active", "Effective Start Date",
					"Effective End date" };
			ByteArrayInputStream in = demoTemplate(header, file_type);
			InputStreamResource file = new InputStreamResource(in);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

		}

		return new ResponseEntity<String>("Not Found", HttpStatus.BAD_REQUEST);

	}

	public static ByteArrayInputStream demoTemplate(String[] HEADERs, String file_type) throws IOException {
		String SHEET = file_type;
		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Sheet sheet = workbook.createSheet(SHEET);
		Row headerRow = sheet.createRow(0);
		for (int col = 0; col < HEADERs.length; col++) {
			Cell cell = headerRow.createCell(col);
			cell.setCellValue(HEADERs[col]);
		}
		workbook.write(out);
		return new ByteArrayInputStream(out.toByteArray());

	}

	@Autowired
	private CSVService csvService;
	@Value("${projectPath}")
	private String projectpath;

//	download Template In Excel
	@GetMapping("/download-csv")
	public ResponseEntity<?> downloadCSV(@RequestParam String tableName) throws IOException {

		System.out.println("downloading start..");
		String processString = processString(tableName);
		String filename = csvService.generateCSV(processString.toLowerCase());

		System.out.println("file generate..");

		String baseFilePath = projectpath + File.separator + filename;

		Path zipFilePath = Paths.get(baseFilePath);
		try {
			byte[] Content = Files.readAllBytes(zipFilePath);

			ByteArrayInputStream in = new ByteArrayInputStream(Content);
			InputStreamResource file = new InputStreamResource(in);
//				ByteArrayResource resource = new ByteArrayResource(zipContent);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			return ResponseEntity.ok().headers(headers).contentLength(Content.length).body(file);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	public String processString(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		StringBuilder result = new StringBuilder();
		result.append(input.charAt(0)); // Append the first character as is

		for (int i = 1; i < input.length(); i++) {
			char currentChar = input.charAt(i);
			char prevChar = input.charAt(i - 1);

			if (Character.isUpperCase(currentChar) && prevChar == '_') {
				result.append(Character.toLowerCase(currentChar));
			} else if (Character.isUpperCase(currentChar)) {
				result.append('_').append(Character.toLowerCase(currentChar));
			} else {
				result.append(currentChar);
			}
		}

		return result.toString();
	}

//	@PostMapping("/save/{filetype}")
//	public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file, @PathVariable String filetype)
//			throws IOException, ParseException {
//		BufferedReader br;
//		InputStream is = file.getInputStream();
//		br = new BufferedReader(new InputStreamReader(is));
//		String file_name = file.getOriginalFilename();
//		AppUser loggedInUser = userService.getLoggedInUser();
//
//		if (filetype.equalsIgnoreCase("priority")) {
//
//			if (file_name.contains("Sr_priority2_t")) {
//
//				String[] header = { "Priority Name", "Description", "Is Active", "Effective Start Date",
//						"Effective End date" };
//				ArrayList<Sr_priority2_t> prioritylist = new ArrayList<Sr_priority2_t>();
//
//				Workbook workbook = WorkbookFactory.create(is);
//
//// 			Create a DataFormatter to format and get each cell's value as String
//				DataFormatter dataFormatter = new DataFormatter();
//
//				Sheet sheet = workbook.getSheetAt(0);
//
//				// Getting number of columns in the Sheet
//				int cols = sheet.getRow(0).getLastCellNum();
//
//				fileupload.uploadFile(file, loggedInUser.getUserId(), filetype);
//
//				for (Row row : sheet) {
//
//					if (row.getRowNum() == 0) {
//						for (int i = 0; i < cols; i++) {
//							String value = dataFormatter.formatCellValue(row.getCell(i));
//
//							if (!header[i].equalsIgnoreCase(value)) {
//
//								return new ResponseEntity<>(
//										"priority file Should  have \"" + header + " \"in the header in excel file",
//										HttpStatus.BAD_REQUEST);
//							}
//						}
//
//					}
//
//					if (row.getRowNum() != 0) {
//						Date date = row.getCell(3).getDateCellValue();
//						Date date2 = row.getCell(4).getDateCellValue();
//
//						Sr_priority2_t priority = new Sr_priority2_t();
//						priority.setPriority_name(dataFormatter.formatCellValue(row.getCell(0)));
//						priority.setDescription(dataFormatter.formatCellValue(row.getCell(1)));
//						priority.setActive(Boolean.valueOf(dataFormatter.formatCellValue(row.getCell(2))));
//
//						priority.setEffective_start_date(date);
//						priority.setEffective_end_date(date2);
//						priorityrepo.save(priority);
//
//						prioritylist.add(priority);
//					}
//
//				}
////				priorityrepo.saveAll(prioritylist);
//				workbook.close();
//
////			
//
//				return new ResponseEntity<>("File Uploaded", HttpStatus.ACCEPTED);
//			}
//			return new ResponseEntity<>("File name should contain Sr_priority2_t", HttpStatus.BAD_REQUEST);
//
//		}
//
//		return new ResponseEntity<>("Something Went Wrong please try again....!!!!! ", HttpStatus.BAD_REQUEST);
//
//	}
//
////	Download template data in excel Files
//
//	@GetMapping("/download/{file_type}")
//
//	public ResponseEntity<?> getFile(@PathVariable String file_type) throws IOException {
//
//		if (file_type.equalsIgnoreCase("priority")) {
//
//			String filename = "Sr_priority2_t" + ".xlsx";
//			String[] header = { "Priority Name", "Description", "Is Active", "Effective Start Date",
//					"Effective End date" };
//			List<Sr_priority2_t> findAll = priorityrepo.findAll();
//			ByteArrayInputStream in = templateByte(findAll, header);
//			InputStreamResource file = new InputStreamResource(in);
//			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
//
//		} else if (file_type.equalsIgnoreCase("impact")) {
//
//			String filename = "Sr_impact2_t" + ".xlsx";
//			String[] header = { "Impact Name", "Description", "Is Active", "Effective Start Date",
//					"Effective End date" };
//			List<Sr_impact2_t> findAll = impactrepo.findAll();
//			ByteArrayInputStream in = templateByte3(findAll, header);
//			InputStreamResource file = new InputStreamResource(in);
//			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
//
//		} 
//
//		return new ResponseEntity<String>("Not Found", HttpStatus.BAD_REQUEST);
//
//	}

	@GetMapping("/getalltemplate")
	public ResponseEntity<?> getALlTemplate() {
		return new ResponseEntity<>(temprepo.findAll(), HttpStatus.ACCEPTED);
	}

	@GetMapping("/gettemplatebyid/{id}")
	public ResponseEntity<?> getTemplateById(@PathVariable Long id) {
		return new ResponseEntity<>(fileupload.getTemplatebyid(id), HttpStatus.ACCEPTED);
	}

	@PostMapping("/save/{entityName}/{name}")
	public ResponseEntity<?> post(@RequestParam(required = false) MultipartFile file, @PathVariable String entityName,
			@PathVariable String name) throws JsonMappingException, JsonProcessingException {

		TemplateFileUpload job;
		AppUser loggedInUser = userService.getLoggedInUser();

//		job = new ObjectMapper().readValue(entityName, TemplateFileUpload.class);

		if (file != null) {
			System.out.println(file.getOriginalFilename());

//			boolean f = 
//					fileUploadService.uploadFile(file);

			fileupload.uploadFile(file, loggedInUser.getUserId(), entityName, name);

		}

		else {
			System.out.println("erro");
		}

//		TemplateFileUpload save = temprepo.save(job);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@DeleteMapping("/deletetemplate/{id}")
	public ResponseEntity<String> deleteTemplateFileUploadById(@PathVariable Long id) {
		try {
			fileupload.deleteTemplateFileUploadById(id);
			return ResponseEntity.ok("Template file deleted successfully");
		} catch (Exception e) {
			// Handle exceptions, e.g., if the entity with the given ID is not found
			return ResponseEntity.badRequest().body("Failed to delete template file: " + e.getMessage());
		}
	}

}
