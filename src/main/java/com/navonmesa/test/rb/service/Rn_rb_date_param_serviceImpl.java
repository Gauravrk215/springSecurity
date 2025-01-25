package com.realnet.rb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.rb.entity.Rn_Rb_Date_String;
import com.realnet.rb.repository.Rn_rb_date_param_repository;

@Service
public class Rn_rb_date_param_serviceImpl implements Rn_rb_date_param_service{

	@Autowired
	private Rn_rb_date_param_repository rn_repo;
	
	@Override
	public List<Rn_Rb_Date_String> save(List<Rn_Rb_Date_String> rn_tables) {
		List<Rn_Rb_Date_String> savedTables = rn_repo
				.saveAll(rn_tables);
		return savedTables;
	}
	
}
