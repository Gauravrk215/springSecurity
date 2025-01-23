package com.realnet.rb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.rb.entity.Rn_Rb_Std_Param;
import com.realnet.rb.repository.Rn_rb_std_repository;

@Service
public class Rn_rb_std_param_serviceImpl implements Rn_rb_std_param_service{

	
	@Autowired
	private Rn_rb_std_repository rn_repo;
	
	@Override
	public List<Rn_Rb_Std_Param> save(List<Rn_Rb_Std_Param> rn_tables) {
		List<Rn_Rb_Std_Param> savedTables = rn_repo
				.saveAll(rn_tables);
		return savedTables;
	}
}
