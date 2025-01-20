package com.realnet.rb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.rb.entity.Rn_rb_Column;
import com.realnet.rb.repository.Rn_column_Repository;
import com.realnet.rb.response.Rn_columnDTO;

@Service
public class Rn_rb_column_serviceImpl implements Rn_rb_column_service{

	
	@Autowired
	private Rn_column_Repository rn_repo;
	
	@Override
	public List<Rn_rb_Column> save(List<Rn_rb_Column> rn_tables) {
		List<Rn_rb_Column> savedTables = rn_repo
				.saveAll(rn_tables);
		return savedTables;
	}

	@Override
	public List<Rn_columnDTO> getListOfColumns(int id) {
		List<Rn_columnDTO> getList=rn_repo.getList(id);
		return getList;
	}
}
