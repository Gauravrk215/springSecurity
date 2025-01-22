package com.realnet.rb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.rb.entity.Rn_Rb_Where_Param;
import com.realnet.rb.entity.Rn_rb_Tables;
import com.realnet.rb.repository.Rn_rb_where_param_repository;

@Service
public class Rn_rb_where_param_serviceImpl implements Rn_rb_where_param_service{

	@Autowired
	private Rn_rb_where_param_repository rn_repo;
	
	@Override
	public List<Rn_Rb_Where_Param> save(List<Rn_Rb_Where_Param> rn_tables) {
		List<Rn_Rb_Where_Param> savedTables = rn_repo
				.saveAll(rn_tables);
		return savedTables;
	}

}
