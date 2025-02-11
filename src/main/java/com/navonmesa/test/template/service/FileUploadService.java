package com.realnet.template.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import com.realnet.template.entity.TemplateFileUpload;
import com.realnet.template.repository.TemplatedataRepo;

@Service
public class FileUploadService {

	@Autowired
	private TemplatedataRepo repo;

	@Value("${projectPath}")
	private String projectPath;

	public String uploadFile(MultipartFile file, Long user_id, String file_type, String name) {
		try {

			String filepath = "import-data";

			String file_name = file.getOriginalFilename();
			String substring = file_name.substring(0, file_name.lastIndexOf("."));

			String str = substring + System.currentTimeMillis();

//			 String location =System.getProperty("user.dir")+filepath;
			String location = projectPath + File.separator + filepath;
			File dir2 = new File(location);
			if (!dir2.exists()) {
				dir2.mkdir();
			}

			String file_name2 = file.getOriginalFilename();

			File staticdir2 = new File(location + "/incomingfile");
			if (!staticdir2.exists()) {
				staticdir2.mkdir();
			}
			String getpath2 = staticdir2.getAbsolutePath();
			System.out.println(getpath2);

			Path copyLocation2 = Paths.get(staticdir2 + File.separator + StringUtils.cleanPath(file_name2));

			Files.copy(file.getInputStream(), copyLocation2, StandardCopyOption.REPLACE_EXISTING);

			File staticdir = new File(location + "/processingfile");
			if (!staticdir.exists()) {
				staticdir.mkdir();
			}
			String getpath = staticdir.getAbsolutePath();
			System.out.println(getpath);

			Path copyLocation = Paths.get(staticdir + File.separator + StringUtils.cleanPath(str + ".xlsx"));

			Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

			TemplateFileUpload exceldata = new TemplateFileUpload();
			exceldata.setFile_location(location);

			exceldata.setFile_name(file.getOriginalFilename());
			exceldata.setFile_changed_name(str);
		//	exceldata.setFile_type(file_type);
			exceldata.setEntity_name(file_type);
			exceldata.setName(name);
			exceldata.setUser_id(user_id);
			repo.save(exceldata);

			System.out.println("-----------------------saving data----------------");
			return location;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!");
		}

	}
	
	
	


	public TemplateFileUpload getTemplatebyid(Long Id) {
		TemplateFileUpload one = repo.getOne(Id);
		return one;

	}

	 public void deleteTemplateFileUploadById(Long id) {
	        // Check if the entity with the given ID exists
	        if (repo.existsById(id)) {
	        	repo.deleteById(id);
	        } 
	 }
	 
	 
	 
	  public TemplateFileUpload getFileById(Long id) {
	        // Use your JPA repository to retrieve the file by its ID
	        return repo.findById(id).orElse(null);
	    }
}
