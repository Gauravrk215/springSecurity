package com.realnet.FromExtensionJson.Services;

import com.realnet.FromExtensionJson.Repository.FromExtensionJson_Repository;
import com.realnet.FromExtensionJson.Entity.FromExtensionJson_t;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FromExtensionJson_Service {
	@Autowired
	private FromExtensionJson_Repository Repository;

	public FromExtensionJson_t Savedata(FromExtensionJson_t data) {
		return Repository.save(data);
	}

	public List<FromExtensionJson_t> getdetails() {
		return (List<FromExtensionJson_t>) Repository.findAll();
	}

	public FromExtensionJson_t getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public FromExtensionJson_t update(FromExtensionJson_t data, Long id) {
		FromExtensionJson_t old = Repository.findById(id).get();
		old.setForm_code(data.getForm_code());
		old.setAccount_id(data.getAccount_id());
		old.setJsonObject(data.getJsonObject());
		final FromExtensionJson_t test = Repository.save(old);
		return test;
	}
}