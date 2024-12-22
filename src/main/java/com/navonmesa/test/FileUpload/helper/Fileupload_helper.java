package com.realnet.FileUpload.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Fileupload_helper {

	@Value("${projectPath}")
	private String projectPath;

	public final String UPLOAD_DIREC = "/Files";

	public boolean uploadFile(MultipartFile multipartFile) {
		boolean f = false;

		try {

			File path = new File(projectPath + UPLOAD_DIREC);
			File FileParentDir = new File(path.toString());
			if (!FileParentDir.exists()) {
				FileParentDir.mkdirs();
			}
			// reading data
			InputStream is = multipartFile.getInputStream();
			byte data[] = new byte[is.available()];
			is.read(data);

			// writing data

			FileOutputStream fos = new FileOutputStream(
					projectPath + UPLOAD_DIREC + File.separator + multipartFile.getOriginalFilename());
			fos.write(data);
			fos.close();
			fos.flush();
			f = true;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return f;
	}

	public boolean uploadFilewithtimestamp(MultipartFile multipartFile, String timestamp) {
		boolean f = false;

		try {

			File path = new File(projectPath + UPLOAD_DIREC);
			File FileParentDir = new File(path.toString());
			if (!FileParentDir.exists()) {
				FileParentDir.mkdirs();
			}
			// reading data
			InputStream is = multipartFile.getInputStream();
			byte data[] = new byte[is.available()];
			is.read(data);

			// writing data

			FileOutputStream fos = new FileOutputStream(
					projectPath + UPLOAD_DIREC + File.separator + timestamp + multipartFile.getOriginalFilename());
			fos.write(data);
			fos.close();
			fos.flush();
			f = true;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return f;
	}

}
