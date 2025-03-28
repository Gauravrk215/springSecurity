package com.realnet.Dashboard_builder.Services;

import com.realnet.Dashboard_builder.Repository.Dashboard_builder_Repository;
import com.realnet.Dashboard_builder.Entity.Dashboard_builder_t;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Dashboard_builder_Service {
	@Autowired
	private Dashboard_builder_Repository Repository;

	public Dashboard_builder_t Savedata(Dashboard_builder_t data) {
		return Repository.save(data);
	}

	public List<Dashboard_builder_t> getdetails() {
		return (List<Dashboard_builder_t>) Repository.findAll();
	}

	public Dashboard_builder_t getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public Dashboard_builder_t update(Dashboard_builder_t data, Long id) {
		Dashboard_builder_t old = Repository.findById(id).get();
		old.setDashboardname(data.getDashboardname());
		final Dashboard_builder_t test = Repository.save(old);
		return test;
	}
}