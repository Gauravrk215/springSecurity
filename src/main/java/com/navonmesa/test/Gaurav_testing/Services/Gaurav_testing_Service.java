package com.realnet.Gaurav_testing.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.Gaurav_testing.Entity.Gaurav_testing_t;
import com.realnet.Gaurav_testing.Repository.Gaurav_testing_Repository;

@Service
public class Gaurav_testing_Service {
	@Autowired
	private Gaurav_testing_Repository Repository;

	public Gaurav_testing_t Savedata(Gaurav_testing_t data) {
		return Repository.save(data);
	}

	public List<Gaurav_testing_t> getdetails() {
		return (List<Gaurav_testing_t>) Repository.findAll();
	}

	public Gaurav_testing_t getdetailsbyId(Integer id) {
		return Repository.findById(id).get();
	}

	public Gaurav_testing_t getdetailcustom() {
		return Repository.findTopByOrderByIdAsc();
	}

	public void delete_by_id(Integer id) {
		Repository.deleteById(id);
	}

	public Gaurav_testing_t update(Gaurav_testing_t data, Integer id) {
		Gaurav_testing_t old = Repository.findById(id).get();
		old.setName(data.getName());
		old.setEmail(data.getEmail());
		old.setMobno(data.getMobno());
		old.setAddress(data.getAddress());
		old.setPincode(data.getPincode());
		old.setDescription(data.getDescription());
		final Gaurav_testing_t test = Repository.save(old);
		return test;
	}

	public Gaurav_testing_t savecustom(Gaurav_testing_t data) {

		Gaurav_testing_t old = new Gaurav_testing_t();
		old.setName(data.getName());
		old.setEmail(data.getEmail());
		old.setMobno(data.getMobno());
		old.setAddress(data.getAddress());
		old.setPincode(data.getPincode());
		final Gaurav_testing_t test = Repository.save(old);
		return test;
	}
}