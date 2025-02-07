package com.realnet.template.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realnet.FileUpload.helper.Fileupload_helper;
import com.realnet.template.entity.ImageUpload;
import com.realnet.template.service.ImageUploadService;


@RequestMapping("api/template/imageUpload")
@RestController
public class ImageUploadController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${projectPath}")
	private String projectPath;

	public final String UPLOAD_DIREC = "/Files";

	@Autowired
	private Fileupload_helper fileuploadhelper;

	@Autowired
	private ImageUploadService imageUploadService;

	@PostMapping("/ImageUpload")
	public ResponseEntity<?> save(@RequestParam String data, @RequestParam MultipartFile file) throws Exception {

		Date date = new Date();
		String str = date.getTime() + "_";

		ImageUpload docs;

		docs = new ObjectMapper().readValue(data, ImageUpload.class);

		if (!file.isEmpty()) {
			System.out.println(file.getOriginalFilename());

			boolean f = fileuploadhelper.uploadFilewithtimestamp(file, str);

			if (f) {
				System.out.println("file uploaded successfully");
				int lastIndexOf = file.getOriginalFilename().lastIndexOf(".");
				String filetype = file.getOriginalFilename().substring(lastIndexOf + 1);
				docs.setFilename(str + file.getOriginalFilename());

				docs.setFilePath(projectPath + UPLOAD_DIREC);

			}

			else {
				docs.setFilename("No file uploaded");
				System.out.println(" no file uploaded ");

			}

		} else {
			docs.setFilename("No file found");
			System.out.println(" no file uploaded ");

		}
		ImageUpload saveddata = imageUploadService.Savedata(docs);

		return new ResponseEntity<>(saveddata, HttpStatus.OK);
	}

	@GetMapping("/ImageUpload")
	public List<ImageUpload> getdetails() {
		List<ImageUpload> get = imageUploadService.getdetails();
		return get;
	}

	@GetMapping("/ImageUpload/{id}")
	public ImageUpload getdetailsbyId(@PathVariable Long id) {
		ImageUpload get = imageUploadService.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/ImageUpload/{id}")
	public void delete_by_id(@PathVariable Long id) {
		imageUploadService.delete_by_id(id);

	}

	@PutMapping("/ImageUpload/{id}")
	public ImageUpload update(@RequestBody ImageUpload data, @PathVariable Long id) {
		ImageUpload update = imageUploadService.update(data, id);
		return update;
	}
}
