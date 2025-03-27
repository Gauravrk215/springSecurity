//package com.realnet.Dashboard_builder.Controllers;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.apache.commons.io.FilenameUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.realnet.Communication.Models.Com_jobTable;
//import com.realnet.Communication.Models.Com_template;
//import com.realnet.Communication.Models.ProcessedJobTable;
//import com.realnet.Communication.Repos.JobTablerepo;
//import com.realnet.Communication.Repos.ProcessedJobTableRepo;
//import com.realnet.Communication.Repos.TemplateRepo;
//import com.realnet.Communication.Services.EmailCommunicationService;
//import com.realnet.Communication.response.EntityResponse;
//import com.realnet.Dashboard_builder.Entity.DashboardSchedule_t;
//import com.realnet.Dashboard_builder.Repository.DashboardSchedule_Repository;
//import com.realnet.Gateway.Entity.Gateway_t;
//import com.realnet.Gateway.Repository.Gateway_Repository;
//import com.realnet.Gateway.Services.Gateway_Service;
//import com.realnet.Gateway.Services.SmsGatwaySmsServices;
//
//@RestController
//@RequestMapping("/dashboard/schedule")
//public class EmailGenerate {
//	
//	
//	
//	@Autowired
//	private SmsGatwaySmsServices gatwaySmsServices;
//	
//	
//	
//	
//
//	@Autowired
//	private JobTablerepo Com_jobTablerepo;
//
//	@Autowired
//	private TemplateRepo templateRepo;
//
//	@Autowired
//	private ProcessedJobTableRepo jobTableRepo;
//
//	@Autowired
//	private EmailCommunicationService emailService;
//	
//	@Autowired
//	private Gateway_Repository gateway_Repository;
//	
//	
////	public final String UPLOAD_DIREC = "/src/main/resources/images";
//	
//	
//	@GetMapping("/sendgatewaydashboard")
//	public ResponseEntity<?> sendGateway() {
//		List<Com_jobTable> get = Com_jobTablerepo.findTopByOrderByIdAsc();
//
//		if (!get.isEmpty()) {
//			for (Com_jobTable com_jobTable : get) {
//				String jobType = com_jobTable.getJob_type();
//				if (jobType.equalsIgnoreCase("EMAIL")) {
//					ResponseEntity<?> response = sendMailGateway(com_jobTable);
//					if (response.getStatusCode() != HttpStatus.OK) {
//						return response;
//					}
//				}
////					else if (jobType.equalsIgnoreCase("SMS")) {
////					ResponseEntity<?> response = sendSmsGateway(com_jobTable);
////					if (response.getStatusCode() != HttpStatus.OK) {
////						return response;
////					}
////				}
//					else {
//					return new ResponseEntity<>(new EntityResponse("Incorrect job type"), HttpStatus.BAD_REQUEST);
//				}
//			}
//			return new ResponseEntity<>(get, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(new EntityResponse("All SMS/Emails already sent"), HttpStatus.OK);
//		}
//	}
//
//	private ResponseEntity<?> sendMailGateway(Com_jobTable com_jobTable) {
//		String gatewayName = com_jobTable.getGatewayName();
//	//	Long id = Long.parseLong(gatewayName);
//		Gateway_t getData=gateway_Repository.findByGatewayName(gatewayName);
//		
//		Long id=getData.getId();
//				
//				
//		String tempName = com_jobTable.getTemplate_name();
//	//	Long tempid = Long.parseLong(tempName);
//
//		Com_template template = templateRepo.findBytemplatename(tempName);
//
//		if (template == null) {
//			return new ResponseEntity<>(new EntityResponse("Template not found"), HttpStatus.NOT_FOUND);
//		}
//
//		String replacement_string = com_jobTable.getReplacement_string();
//		String replace_body = template.getBody().replace("<replacement_string>", replacement_string);
//
//		
//
//		ProcessedJobTable gatewayJobTable = new ProcessedJobTable();
//		gatewayJobTable.setJob_type(com_jobTable.getJob_type());
//		gatewayJobTable.setSend_to(com_jobTable.getSend_to());
//		gatewayJobTable.setCc(com_jobTable.getCc());
//		gatewayJobTable.setAttachment(com_jobTable.getAttachment());
//		gatewayJobTable.setGatewaydone(com_jobTable.getGatewaydone());
//		gatewayJobTable.setTemplate_name(com_jobTable.getTemplate_name());
//		gatewayJobTable.setReplacement_string(com_jobTable.getReplacement_string());
//		gatewayJobTable.setGatewayName(com_jobTable.getGatewayName());
//
//		
//		String filename = com_jobTable.getAttachment();
//		if(filename!=null) {
//			
//			Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
//			String filepath = Paths.get(path.toString(), UPLOAD_DIREC, filename).toString();
//			File file = new File(filepath);
//			
//			
//			boolean sendMailWithAttachment = emailService.sendEmailGatewayWithAttachment(id, com_jobTable.getSend_to(), template.getSubject(), replace_body, com_jobTable.getCc(), file);
//
//			System.out.println("email sent with attachment " + sendMailWithAttachment);
//
//			gatewayJobTable.setStatus(HttpStatus.OK.value());
//			gatewayJobTable.setResp_body("Email sent with attachment");
//		
//		}
//		
//		else {
//			
//			boolean sendemail = emailService.sendEmailGateway(id, com_jobTable.getSend_to(), template.getSubject(),
//					replace_body, com_jobTable.getCc());
//			System.out.println("email without attachment sent " + sendemail);
//
//			gatewayJobTable.setStatus(HttpStatus.OK.value());
//			gatewayJobTable.setResp_body("Email sent without attachment");
//		
//			
//		}
//		
//		
//
//		jobTableRepo.save(gatewayJobTable);
//		Com_jobTablerepo.delete(com_jobTable);
//		return new ResponseEntity<>(HttpStatus.OK);
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
//	
////	public final String UPLOAD_DIREC = "/src/main/resources/images";
//	
//	public final String UPLOAD_DIREC = "C:/Users/Gyanadipta Pahi/Desktop/SureSetuLast/suresetu-mohasin205/backend/src/main/resources/images";
//	
//	@PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is required.");
//        }
//
//        try {
//            String fileName = generateFileName(file.getOriginalFilename());
//            String filePath = UPLOAD_DIREC + File.separator + fileName;
//            File dest = new File(filePath);
//            file.transferTo(dest);
//
//            return ResponseEntity.ok("File uploaded successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
//        }
//    }
//
//    private String generateFileName(String originalFilename) {
//        String baseName = "dashboardSchedule" + Instant.now().getEpochSecond();
//        String extension = FilenameUtils.getExtension(originalFilename);
//        if (!StringUtils.isEmpty(extension)) {
//            baseName += "." + extension;
//        }
//        return baseName;
//    }
//	
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
////    @GetMapping("/files/{partialName}")
////    public ResponseEntity<byte[]> getFileByName(@PathVariable("partialName") String partialName) {
////        List<File> matchingFiles = findMatchingFiles(partialName);
////
////        if (matchingFiles.isEmpty()) {
////            return ResponseEntity.notFound().build();
////        } else if (matchingFiles.size() > 1) {
////       //     return ResponseEntity.status(HttpStatus.CONFLICT).body("Multiple files match the provided name.");
////        }
////
////        File file = matchingFiles.get(0);
////        try {
////            byte[] fileBytes = Files.readAllBytes(file.toPath());
////
////            HttpHeaders headers = new HttpHeaders();
////            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
////            headers.setContentDispositionFormData("attachment", file.getName());
////            headers.setContentLength(fileBytes.length);
////
////            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
////        } catch (Exception e) {
////            e.printStackTrace();
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
////        }
////    }
////
////    private List<File> findMatchingFiles(String partialName) {
////        List<File> matchingFiles = new ArrayList<>();
////
////        File directory = new File(UPLOAD_DIREC);
////        File[] files = directory.listFiles();
////        if (files != null) {
////            for (File file : files) {
////                if (file.isFile() && file.getName().startsWith(partialName)) {
////                    matchingFiles.add(file);
////                }
////            }
////        }
////
////        return matchingFiles;
////    }
//    
////    @Autowired
////    private JobTablerepo  comJobTableRepository;
//    
//    @Autowired
//    private DashboardSchedule_Repository  dashboardSchedule_Repository;
//    
//    @GetMapping("/files/{partialName}")
//    public ResponseEntity<byte[]> getFileByName(@PathVariable("partialName") String partialName) {
//        List<File> matchingFiles = findMatchingFiles(partialName);
//
//        if (matchingFiles.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else if (matchingFiles.size() > 1) {
//        	System.out.println("Multiple files match the provided name");
//         //   return ResponseEntity.status(HttpStatus.CONFLICT).body("Multiple files match the provided name.");
//        }
//
//        File file = matchingFiles.get(0);
//        try {
//            byte[] fileBytes = Files.readAllBytes(file.toPath());
//
//            // Save file path as attachment in the entity
////            List<DashboardSchedule_t> get = dashboardSchedule_Repository.findTopByOrderByIdAsc();
////            get.setAttachment(file.getAbsolutePath());
////            dashboardSchedule_Repository.save(comJobTable);
//            
//       //     // Replace with the desired DashboardSchedule_t entity ID
//            List<DashboardSchedule_t> dashboardSchedules = dashboardSchedule_Repository.findTopByOrderByIdAsc();
//            if (!dashboardSchedules.isEmpty()) {
//                DashboardSchedule_t dashboardSchedule = dashboardSchedules.get(0);
//                dashboardSchedule.setAttachment(file.getAbsolutePath());
//                dashboardSchedule.setGatewaydone("N");
//                dashboardSchedule_Repository.save(dashboardSchedule);
//            } else {
//                System.out.println("No dashboard schedules found.");
//            }
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentDispositionFormData("attachment", file.getName());
//            headers.setContentLength(fileBytes.length);
//
//            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    private List<File> findMatchingFiles(String partialName) {
//        List<File> matchingFiles = new ArrayList<>();
//
//        File directory = new File(UPLOAD_DIREC);
//        File[] files = directory.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isFile() && file.getName().startsWith(partialName)) {
//                    matchingFiles.add(file);
//                }
//            }
//        }
//
//        return matchingFiles;
//    }
//    
//    
//    
//    
//    
//
//    
//    
//    @GetMapping("/dashboardscheduleid")
//    public List<Com_jobTable> getDashboardScheduleId() {
//        List<DashboardSchedule_t> dashboardScheduleOptional = dashboardSchedule_Repository.findTopByOrderByIdAsc();
//        
//        List<DashboardSchedule_t> filteredList = dashboardScheduleOptional.stream()
//                .filter(dashboardSchedule -> dashboardSchedule.getAttachment() != null && !dashboardSchedule.getAttachment().isEmpty())
//                .collect(Collectors.toList());
//        
//        List<Com_jobTable> comJobTables = new ArrayList<>();
//        for (DashboardSchedule_t dashboardSchedule : filteredList) {
//            Com_jobTable comJobTable = new Com_jobTable();
//            comJobTable.setAttachment(dashboardSchedule.getAttachment());
//            comJobTable.setGatewaydone(dashboardSchedule.getGatewaydone());
//            comJobTable.setSend_to(dashboardSchedule.getSendTo());
//            comJobTable.setTemplate_name(dashboardSchedule.getTemplate());
//            comJobTable.setGatewayName(dashboardSchedule.getGateway());
//            comJobTable.setCc(dashboardSchedule.getCc());
//            comJobTable.setReplacement_string(dashboardSchedule.getReplacementString());
//            comJobTable.setJob_type(dashboardSchedule.getType());
//           
//            
//            comJobTables.add(comJobTable);
//        }
//        
//        List<Com_jobTable> savedComJobTables = Com_jobTablerepo.saveAll(comJobTables);
//        
//        
//        for (DashboardSchedule_t dashboardSchedule : filteredList) {
//            dashboardSchedule.setAttachment(null);
//            dashboardSchedule.setGatewaydone("Y");
//        }
//        dashboardSchedule_Repository.saveAll(filteredList);
//        
//        return savedComJobTables;
//    }
//
//
//
//
//
//
//
//
//
//}
