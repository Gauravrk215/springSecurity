//package com.realnet.FileUpload.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.realnet.fileupload.helper.Fileupload_helper;
//
//@RestController
//@RequestMapping("/sureserve/api")
//public class Filecontroller {
//
//	@Autowired
//	private Fileupload_helper fileuploadhelper;
//
//	@PostMapping("/test")
//	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
//
//		/*
//		 * System.out.println(file.getOriginalFilename());
//		 * System.out.println(file.getSize());
//		 * System.out.println(file.getContentType());
//		 */
//
//		try {
//
//			if (file.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("must contain file");
//			}
//			/*
//			 * if(!file.getContentType().equals("image/png")) {
//			 * 
//			 * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
//			 * body("must contain png format"); }
//			 */
//
//			boolean f = fileuploadhelper.uploadFile(file);
//			if (f) {
//				return ResponseEntity.ok("File uploaded successfully");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
//
//	}
//
//}
