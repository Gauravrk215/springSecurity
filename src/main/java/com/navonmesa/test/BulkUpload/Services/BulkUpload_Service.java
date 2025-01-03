package com.realnet.BulkUpload.Services;

import com.realnet.BulkUpload.Repository.BulkUpload_Repository;
import com.realnet.BulkUpload.Entity.BulkUpload_t;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkUpload_Service {
	@Autowired
	private BulkUpload_Repository Repository;

	public BulkUpload_t Savedata(BulkUpload_t data) {
		return Repository.save(data);
	}

	public List<BulkUpload_t> getdetails() {
		return (List<BulkUpload_t>) Repository.findAll();
	}

	public BulkUpload_t getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public BulkUpload_t update(BulkUpload_t data, Long id) {
		BulkUpload_t old = Repository.findById(id).get();
		old.setEntity_name(data.getEntity_name());
		old.setDescription(data.getDescription());
		old.setRule_line(data.getRule_line());
		old.setActive(data.isActive());
		final BulkUpload_t test = Repository.save(old);
		return test;
	}
}