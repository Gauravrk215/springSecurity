package com.realnet.FileUpload.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileuploadService {
	@Value("${projectPath}")
	private String projectPath;

	public boolean uploadFile(MultipartFile multipartFile, String addString) {
		boolean f = false;
		String UPLOAD_DIREC = File.separator + "Resources" + File.separator + "Files";
		String originalFilename = multipartFile.getOriginalFilename();

		String filetype = originalFilename.substring(originalFilename.lastIndexOf("."));
		String filename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) + addString;
		String replacedfilename = filename + filetype;

		System.out.println("file name  with replace is ..." + replacedfilename);
		String Path1 = projectPath + UPLOAD_DIREC;

		String filepath = Path1 + File.separator + replacedfilename;

		try {

			if (!UPLOAD_DIREC.isEmpty()) {

				File projectdir = new File(Path1);
				if (!projectdir.exists()) {
					boolean mkdir = projectdir.mkdirs();
					System.out.println(Path1 + "  folder create =  " + mkdir);
				}


			}
			// reading data
			InputStream is = multipartFile.getInputStream();
			byte data[] = new byte[is.available()];
			is.read(data);

			// writing data

			FileOutputStream fos = new FileOutputStream(filepath);
			fos.write(data);
			fos.close();
			fos.flush();
			f = true;

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
		}
		return f;
	}

}
