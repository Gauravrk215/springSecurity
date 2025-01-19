package com.realnet.rb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.rb.entity.Rn_Rb_Adhoc;
import com.realnet.rb.repository.Rn_rb_adhoc_repository;

@Service
public class Rn_rb_adhoc_param_serviceImpl implements Rn_rb_adhoc_param_service{

	@Autowired
	private Rn_rb_adhoc_repository rn_repo;
	
	@Override
	public List<Rn_Rb_Adhoc> save(List<Rn_Rb_Adhoc> rn_tables) {
		List<Rn_Rb_Adhoc> savedTables = rn_repo
				.saveAll(rn_tables);
		return savedTables;
	}
}
