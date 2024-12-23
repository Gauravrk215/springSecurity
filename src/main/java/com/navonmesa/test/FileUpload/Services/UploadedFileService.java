package com.realnet.FileUpload.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.FileUpload.Entity.Uploadeddocs;
import com.realnet.FileUpload.Repository.UploadFileRepository;

@Service
public class UploadedFileService {
	@Autowired
	private UploadFileRepository Repository;

	public Uploadeddocs Savedata(Uploadeddocs data) {
		return Repository.save(data);
	}

	public List<Uploadeddocs> getdetails() {
		return (List<Uploadeddocs>) Repository.findAll();
	}

	public Uploadeddocs getdetailsbyId(Integer id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Integer id) {
		Repository.deleteById(id);
	}

	public Uploadeddocs update(Uploadeddocs data, Integer id) {
		Uploadeddocs old = Repository.findById(id).get();
		old.setUploadedfile_name(data.getUploadedfile_name());
		final Uploadeddocs test = Repository.save(old);
		return test;
	}

//	get by ref and table name
	public List<Uploadeddocs> getbyrefandtablename(String ref, String ref_table_name) {
		return (List<Uploadeddocs>) Repository.findbyrefAnsTableName(ref, ref_table_name);
	}
}
