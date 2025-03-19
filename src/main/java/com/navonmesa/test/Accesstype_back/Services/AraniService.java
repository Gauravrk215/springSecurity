package com.realnet.Accesstype_back.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.Accesstype_back.Entity.Arani;
import com.realnet.Accesstype_back.Repository.AraniRepository;

@Service
public class AraniService {
	@Autowired
	private AraniRepository repo;
	
	 public List<Arani> getAll() {
	       return (List<Arani>) repo.findAll();
	    }

	 public Arani getdetailsbyid(Long id) {
		 return repo.findById(id).get();
	 }
	 
	 public Arani saverani(Arani rani){
		 Arani saverani = repo.save(rani);
		return saverani;
	 }
	 
	 public void delete_by_id(Long id) {
		 repo.deleteById(id);
	 }
	 
	 public Arani update(Arani data, Long id) {
		 Arani old = repo.findById(id).get();
		 
		 old.setName(data.getName());
		 old.setEmail(data.getEmail());
		 old.setAddress(data.getAddress());
		 
		 final Arani test = repo.save(old);
		return test;
		 
	 }
}
