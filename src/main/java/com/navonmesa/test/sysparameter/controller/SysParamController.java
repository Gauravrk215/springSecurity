package com.realnet.sysparameter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.sysparameter.entity.SysParamEntity;
import com.realnet.sysparameter.entity.SysParamUpload;
import com.realnet.sysparameter.repository.SysParamRepository;
import com.realnet.sysparameter.service.SysParamService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/sysparam")
@Api(tags = { "sysparameter" })
public class SysParamController {

	@Autowired
	private SysParamService sysparamService;

	@Autowired
	private SysParamRepository sysParamRepo;

	@Value("${projectPath}")
	private String projectPath;

	// GET BY ID
	@ApiOperation(value = "Get A system parameters", response = SysParamEntity.class)
	@GetMapping("/getSysParams/{id}")
	public ResponseEntity<SysParamEntity> getSystemParamDetails(@PathVariable(value = "id") int id) {
		SysParamEntity sysparameter = sysparamService.getById(id);
		if (sysparameter == null) {
			throw new ResourceNotFoundException("system parameter not found with id " + id);
		}
		return ResponseEntity.ok().body(sysparameter);
	}

	// ADD SYSTEM PARAMETER
	@ApiOperation(value = "Add New system parameters")
	@PostMapping(value = "/addSysParams", consumes = { "application/json" })
	public ResponseEntity<?> saveFavourite(@RequestBody SysParamEntity sysparam, HttpSession session1)
			throws IOException {

		SysParamEntity savedsysparam = sysparamService.save(sysparam);
		System.out.println("save system parameter id" + savedsysparam);

		if (savedsysparam == null) {
			throw new ResourceNotFoundException("system parameter not saved ");
		}
		return ResponseEntity.ok().body(savedsysparam);
	}

	// UPDATE
	@ApiOperation(value = "update a system parameters", response = SysParamEntity.class)
	@PutMapping("/updateSysParams/{id}")
	public ResponseEntity<SysParamEntity> updateSystemParameter(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") Integer id, @Valid @RequestBody SysParamEntity sysparam) {
		SysParamEntity updatedsale = sysparamService.updateSysParamById(id, sysparam);
		if (updatedsale == null || id != updatedsale.getId()) {
			throw new ResourceNotFoundException("system parameter not found with id " + id);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
	}

	// UPDATE logo on project
	@PutMapping("/logo/updateSysParams/{sys_id}")
	public ResponseEntity<SysParamEntity> updateLogo(@PathVariable(value = "sys_id") Integer sys_id,
			@Valid @RequestBody SysParamEntity sysparam) throws JsonProcessingException {
		SysParamEntity updatedsale = sysparamService.updatelogo(sys_id, sysparam);
		if (updatedsale == null || sys_id != updatedsale.getId()) {
			throw new ResourceNotFoundException("system parameter not found with id " + sys_id);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
	}

	@PostMapping("/uploadimage")
//	public ResponseEntity<?> fileupload(
//			
//		@RequestParam("image") MultipartFile image) throws IOException {
//		
//		String filename = this.sysparamService.uplaodImage(path,image);
////		System.out.println("Original Image Byte Size - " + file.getBytes().length);
////		CardImage img = new CardImage(file.getOriginalFilename(), file.getContentType(),
////				compressBytes(file.getBytes()));
////		this.imageRepository.save(img);
//	
//		return new  ResponseEntity<>(filename, HttpStatus.OK);
//	}

	public ResponseEntity<?> uploadProfilePic(@RequestParam("imageFile") MultipartFile file) throws IOException {
		SysParamEntity sys = new SysParamEntity();
		String uploadPath = projectPath.concat("/src/main/resources/logo/");
		System.out.println("UPLOAD PATH = " + uploadPath);

		boolean success = sysparamService.upload_logo(file, uploadPath);

		String fileName = file.getOriginalFilename();
//		 String head = fileName.substring(0, fileName.indexOf("."));
		String ext = fileName.substring(fileName.lastIndexOf("."));
		String fileNewName = "profile-pic-" + ext;

		System.out.println(
				"UPLOAD PATH = " + uploadPath + "\nFILE NAME = " + fileNewName + "\nFile Size = " + file.getSize());
		sys.setUpload_Logo(fileNewName);
		Map<String, Boolean> res = new HashMap<String, Boolean>();
		if (success) {
			res.put("success", success);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			res.put("success", success);
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
	}

//	 save data nad upload files
	@PostMapping("/Sysupload")
	public ResponseEntity<?> add(@RequestParam String o1, @RequestParam Map<String, MultipartFile> attachmentsfile)
			throws IOException {
		SysParamEntity sysParamEntity;
		sysParamEntity = new ObjectMapper().readValue(o1, SysParamEntity.class);
		SysParamEntity order = sysparamService.save(sysParamEntity);

		if (!attachmentsfile.isEmpty()) {
//			long cLong = 1;
			ArrayList<SysParamUpload> attachments = new ArrayList<SysParamUpload>();
			for (Map.Entry<String, MultipartFile> e : attachmentsfile.entrySet()) {

				System.out.println(e.getKey());
				SysParamUpload a = new SysParamUpload();
				a.setSysParamEntity(order);
				a.setAttachmentId(order.getId());
				a.setAttachment(e.getValue().getBytes());
				a.setAttachmentType(e.getValue().getContentType());
				a.setAttachmentFilename(e.getValue().getOriginalFilename());
				attachments.add(a);
				order.setUpload_Logo(a.getAttachmentFilename());
//			cLong++;

			}
			sysparamService.addallattachments(attachments);

		}

		return new ResponseEntity<>(order, HttpStatus.OK);

	}

	@PutMapping("/Sysupload/{id}")
	public ResponseEntity<?> update(@RequestParam String o1, @RequestParam Map<String, MultipartFile> attachmentsfile,
			@PathVariable int id) throws IOException {
		SysParamEntity sysparam;
		sysparam = new ObjectMapper().readValue(o1, SysParamEntity.class);
		SysParamEntity order = sysparamService.updateSysParamById(id, sysparam);

		if (!attachmentsfile.isEmpty()) {
//			long cLong = 1;
			ArrayList<SysParamUpload> attachments = new ArrayList<SysParamUpload>();
			for (Map.Entry<String, MultipartFile> e : attachmentsfile.entrySet()) {

				System.out.println(e.getKey());
				SysParamUpload a = new SysParamUpload();
				a.setSysParamEntity(order);
				a.setAttachmentId(order.getId());
				a.setAttachment(e.getValue().getBytes());
				a.setAttachmentType(e.getValue().getContentType());
				a.setAttachmentFilename(e.getValue().getOriginalFilename());
				attachments.add(a);
				order.setUpload_Logo(a.getAttachmentFilename());
//			cLong++;

			}
			sysparamService.addallattachments(attachments);

		}

		return new ResponseEntity<>(order, HttpStatus.OK);

	}

//	 GET JSON WITH PHOTO BY ID
	@GetMapping("/sysparam/{id}")

	public ResponseEntity<?> GETSYS(@PathVariable int id) {
		SysParamEntity pm = sysParamRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("id not found"));

		List<SysParamUpload> attachments = sysparamService.getallattachmentsbyid(id);
		pm.setSysParamUploads(attachments);

		return new ResponseEntity<>(pm, HttpStatus.OK);
	}
}
