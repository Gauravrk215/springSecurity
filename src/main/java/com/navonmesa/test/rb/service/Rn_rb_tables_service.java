package com.realnet.rb.service;

import java.util.List;

import com.realnet.rb.entity.Rn_rb_Tables;
import com.realnet.rb.entity.Rn_report_builder;

public interface Rn_rb_tables_service {
	Rn_report_builder save(Rn_report_builder rn_tables);
	
	List<Rn_rb_Tables> save(List<Rn_rb_Tables> rn_tables);
	
	List<String> getListOfTables();
	List<String> getListOfColumns(int id);
	
	List<String> getColumnList(int id);
	List<String> getColumnAliasList(String name);

	List<String> getListOftables();

	List<String> getdatabaseList();

	List<String> getColumnAliasList1(String table_schema, String tABLE_NAME);

	List<String> getListOftable(String table_schema);
	
	List<String> getColumnAliasList2(String tableName);


}
