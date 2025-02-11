package com.realnet.template.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.template.entity.ImageUpload;
import com.realnet.template.repository.ImageUploadRepo;

@Service
public class ImageUploadService {

	@Autowired
	private ImageUploadRepo Repository;

	public ImageUpload Savedata(ImageUpload data) {
		return Repository.save(data);
	}

	public List<ImageUpload> getdetails() {
		return (List<ImageUpload>) Repository.findAll();
	}

	public ImageUpload getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public ImageUpload update(ImageUpload data, Long id) {
		ImageUpload old = Repository.findById(id).get();
		// old.setTransactionDate(data.getTransactionDate());
//		old.setTransactionAmount(data.isTransactionAmount());
		final ImageUpload test = Repository.save(old);
		return test;
	}

}
