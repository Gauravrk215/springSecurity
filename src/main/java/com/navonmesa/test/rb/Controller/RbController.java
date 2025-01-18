package com.realnet.rb.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.realnet.CredentialDatabase.Service.SurevaultService;
import com.realnet.rb.repository.Rn_report_builder_repository;
import com.realnet.rb.service.Rn_rb_tables_service;

@RestController
public class RbController {

	@Autowired
	private Rn_report_builder_repository rn_table_repository;
	@Autowired
	private Rn_rb_tables_service rn_table_service;

//	@Value("${spring.datasource.username}")
//	private String userName;
//
//	@Value("${spring.datasource.password}")
//	private String password;
//
//	@Value("${spring.datasource.url}")
//	private String url;

	@Autowired
	private SurevaultService databaseCredentialsService;

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
		
		// changes by Gyanadipta
		String url = databaseCredentialsService.getSurevaultCredentials("databaseUrl");
		String userName = databaseCredentialsService.getSurevaultCredentials("databaseuserName");
		String password = databaseCredentialsService.getSurevaultCredentials("databasePassword");
			
		String query = "CREATE SCHEMA " + table_schema + ";";

		List<Integer> list = new ArrayList<Integer>();
		try (Connection con = DriverManager.getConnection(url, userName, password); // conn.str

				Statement stmt = con.createStatement()) {
			int rs = stmt.executeUpdate(query);

			list.add(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return list;

	}

	// get all column list available
	@GetMapping("/AllTable_list/{table_schema}")
	@ResponseBody
	public List<String> getallcolwithalltable(@PathVariable String table_schema, @RequestParam String str) {
		ArrayList<String> tables = new ArrayList<>();

		String liString = "," + str;
		int i = 0;
		do {

			int lastIndexOf = liString.lastIndexOf(",");

			String substring = liString.substring(lastIndexOf + 1);
			tables.add(substring);

			System.out.println(substring);

			liString = liString.substring(0, lastIndexOf);

			System.out.println("step " + i + " = " + liString);
			i++;

		} while (liString.contains(","));

		ArrayList<String> arrayList = new ArrayList<>();
		for (String TABLE_NAME : tables) {
			List<String> list = rn_table_service.getColumnAliasList1(table_schema, TABLE_NAME);
			if (!list.isEmpty()) {
				list.forEach(l -> arrayList.add(l));
			}

		}
		return arrayList;
	}

	@GetMapping("/Alias_Table_list/{table_names}")
	@ResponseBody
	public List<String> getallcolfromalltable(@PathVariable String table_names) {
		List<String> columnNames = rn_table_service.getColumnAliasList2(table_names);
		return columnNames;
	}
}
