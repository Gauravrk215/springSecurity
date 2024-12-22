package com.realnet.FileUpload.Controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.realnet.FileUpload.Entity.Uploadeddocs;
import com.realnet.FileUpload.Services.FileuploadService;
import com.realnet.FileUpload.Services.UploadedFileService;

@RequestMapping(value = "/FileUpload")
//@CrossOrigin("*")
@RestController
public class UploadeddocsController {
	@Autowired
	private UploadedFileService Service;

	@Value("${projectPath}")
	private String projectPath;

	@Autowired
	private FileuploadService fileuploadService;

	@PostMapping("/Uploadeddocs/{ref}/{table_name}")

	public ResponseEntity<?> Savedata(@PathVariable String ref, @PathVariable String table_name,
			@RequestParam MultipartFile file) throws JsonMappingException, JsonProcessingException {

		Uploadeddocs tdata = new Uploadeddocs();
		Date d = new Date();
		String addString = "_" + d.getTime();

		if (file.isEmpty()) {
			return new ResponseEntity<>("file is empty", HttpStatus.BAD_REQUEST);

		}
		String UPLOAD_DIREC = File.separator + "Resources" + File.separator + "Files";
		String originalFilename = file.getOriginalFilename();
		String filetype = originalFilename.substring(originalFilename.lastIndexOf("."));
		String filename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) + addString;
		String replacedfilename = filename + filetype;
		System.out.println("file name is ..." + replacedfilename);

		Uploadeddocs save = null;
		System.out.println(file.getOriginalFilename());

		boolean f = fileuploadService.uploadFile(file, addString);

		if (f) {
			System.out.println("file uploaded successfully");

			tdata.setUploadedfile_path(projectPath + UPLOAD_DIREC);

			tdata.setUploadedfile_name(replacedfilename);
			tdata.setRef(ref);
			tdata.setRef_table_name(table_name);

			save = Service.Savedata(tdata);
			return new ResponseEntity<>(save, HttpStatus.OK);

		}

		else {
			return new ResponseEntity<>("file upload failed", HttpStatus.BAD_REQUEST);

		}

	}

	@PutMapping("/Uploadeddocs/{id}")
	public Uploadeddocs update(@RequestBody Uploadeddocs data, @PathVariable Integer id) {
		Uploadeddocs update = Service.update(data, id);
		return update;
	}

	@GetMapping("/Uploadeddocs")
	public List<Uploadeddocs> getdetails() {
		List<Uploadeddocs> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Uploadeddocs/{id}")
	public Uploadeddocs getdetailsbyId(@PathVariable Integer id) {
		Uploadeddocs get = Service.getdetailsbyId(id);
		return get;
	}

	@DeleteMapping("/Uploadeddocs/{id}")
	public void delete_by_id(@PathVariable Integer id) {
		Service.delete_by_id(id);

	}

//	get by ref and ref table name
	@GetMapping("/Uploadeddocs/{ref}/{ref_tablename}")
	public List<Uploadeddocs> getbyrefandtablename(@PathVariable String ref, @PathVariable String ref_tablename) {
		List<Uploadeddocs> get = Service.getbyrefandtablename(ref, ref_tablename);
		return get;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping("/execute")
	public ResponseEntity<String> executeDump(@RequestParam Boolean execute, @RequestParam String filePath) {
		// Check if execution is allowed
		if (!execute) {
			return ResponseEntity.ok("Service not executed due to 'execute' flag being false.");
		}

		Path path = Paths.get(System.getProperty("user.dir")).resolve(filePath);

		try {
			// Check if file exists
			if (!Files.exists(path)) {
				return ResponseEntity.badRequest().body("File not found: " + filePath);
			}

			// File content ko read karo aur SQL commands extract karo
			String sql = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

			// SQL commands ko execute karo

			// Split statements by semicolon
			List<String> sqlStatements = Arrays.stream(sql.split(";")).map(String::trim)
					.filter(statement -> !statement.isEmpty()) // Empty statements ko remove karo
					.filter(statement -> !statement.startsWith("USE")) // "USE db;" ko ignore karo
					.filter(statement -> !statement.startsWith("--")) // Comments ko ignore karo
					.filter(statement -> !statement.startsWith("/*")) // Special MySQL commands ko ignore karo
					.collect(Collectors.toList());

			// Execute each statement
			for (String statement : sqlStatements) {
				try {
					jdbcTemplate.execute(statement);

					System.out.println(statement + " executed..");

				} catch (DataAccessException e) {
					// Specific SQL execution error ko catch karo aur log karo
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Error executing statement: " + statement + " | Error: " + e.getMessage());
				}
			}
			System.out.println("Dump executed executed..");

			// File ko delete karo
			Files.delete(path);

			System.out.println("File delete successfully...");

			return ResponseEntity.ok("Dump executed and file deleted successfully!");

		} catch (IOException e) {

			System.out.println("io error.." + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("File reading/deletion error: " + e.getMessage());
		} catch (DataAccessException e) {
			System.out.println("DataAccessException error.." + e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("SQL execution error: " + e.getMessage());
		}
	}

}