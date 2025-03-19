package com.realnet.Accesstype_back.Services;

import com.realnet.Accesstype_back.Repository.AccesstypeRepository;
//import com.realnet.AudiTrail.Service.AuditrailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.realnet.Accesstype_back.Entity.Accesstype;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccesstypeService {
	@Autowired
	private AccesstypeRepository Repository;
	
//	@Autowired
//	private AuditrailService auditrailService;

	public Accesstype Savedata(Accesstype data) {
		return Repository.save(data);
	}

	public List<Accesstype> getdetails() {
		return (List<Accesstype>) Repository.findAll();
	}

	public Accesstype getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public Accesstype update(Accesstype data, Long id) {
		Accesstype old = Repository.findById(id).get();
		old.setName(data.getName());
		old.setDescription(data.getDescription());
		final Accesstype test = Repository.save(old);
//		try {
//			auditrailService.saveaudiTrail_t(old, test,"Accesstype");
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return test;
	}
}