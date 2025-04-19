package com.realnet.rb.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.realnet.rb.repository.Rn_report_builder_repository;
import com.realnet.rb.service.Rn_rb_tables_service;

@RestController
public class RbController {

	@Autowired
	private Rn_report_builder_repository rn_table_repository;
	@Autowired
	private Rn_rb_tables_service rn_table_service;

// get all databse list available
	@GetMapping("/Table_list")
	@ResponseBody
	public List<Object> getdatabase() {
		List<Object> list = rn_table_repository.getdatabaseList();
		return list;
	}

	// get all table list available
	@GetMapping("/Table_list/{table_schema}")
	@ResponseBody
	public List<String> gettableList(@PathVariable String table_schema) {
		List<String> list = rn_table_repository.getListOftables(table_schema);
//		 List<String> list = rn_table_service.getListOftable(table_schema);
		return list;
	}

	// get all column list available
	@GetMapping("/Table_list/{table_schema}/{TABLE_NAME}")
	@ResponseBody
	public List<String> getallcolumnlist(@PathVariable String table_schema, @PathVariable String TABLE_NAME) {
		List<String> list = rn_table_service.getColumnAliasList1(table_schema, TABLE_NAME);
		return list;
	}

	// create database
	@GetMapping("/createdatabase/{table_schema}")
	@ResponseBody
	public List<Integer> createdatabase(@PathVariable String table_schema) throws JsonProcessingException {

		List<Integer> list = rn_table_service.createdatabase(table_schema);
		return list;

	}

	// get all column list available
	@GetMapping("/AllTable_list/{table_schema}")
	@ResponseBody
	public List<String> getallcolwithalltable(@PathVariable String table_schema, @RequestParam String str) {
		List<String> arrayList = rn_table_service.getallcolwithalltable(table_schema, str);
		return arrayList;
	}

	@GetMapping("/Alias_Table_list/{table_names}")
	@ResponseBody
	public List<String> getallcolfromalltable(@PathVariable String table_names) {
		List<String> columnNames = rn_table_service.getColumnAliasList2(table_names);
		return columnNames;
	}
}
