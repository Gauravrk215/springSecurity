package com.realnet.Rpt_builder.Services;

import com.realnet.Rpt_builder.Repository.Rpt_builder_Repository;
import com.realnet.Rpt_builder.Entity.Rpt_builder_t;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Rpt_builder_Service {
	@Autowired
	private Rpt_builder_Repository Repository;

	public Rpt_builder_t Savedata(Rpt_builder_t data) {
		return Repository.save(data);
	}

	public List<Rpt_builder_t> getdetails() {
		return (List<Rpt_builder_t>) Repository.findAll();
	}

	public Rpt_builder_t getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public Rpt_builder_t update(Rpt_builder_t data, Long id) {
		Rpt_builder_t old = Repository.findById(id).get();
		old.setName(data.getName());
		old.setFolder(data.getFolder());
		old.setQuery(data.getQuery());
		old.setDate_param_flag(data.isDate_param_flag());
		old.setAdhoc_param_flag(data.isAdhoc_param_flag());
		old.setAdhoc_param_string(data.getAdhoc_param_string());
		old.setStd_param_json(data.getStd_param_json());
		final Rpt_builder_t test = Repository.save(old);
		return test;
	}
}