package com.realnet.FabricIcard.Services;

import com.realnet.FabricIcard.Entity.FabricIcard;
import com.realnet.FabricIcard.Repository.FabricIcardRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FabricIcardService {
	@Autowired
	private FabricIcardRepository Repository;

	public FabricIcard Savedata(FabricIcard data) {
		return Repository.save(data);
	}

	public List<FabricIcard> getdetails() {
		return (List<FabricIcard>) Repository.findAll();
	}

	public FabricIcard getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public FabricIcard update(FabricIcard data, Long id) {
		FabricIcard old = Repository.findById(id).get();
		old.setName(data.getName());
		old.setDescription(data.getDescription());
		old.setActive(data.isActive());
		old.setUrl(data.getUrl());
		final FabricIcard test = Repository.save(old);
		return test;
	}
}