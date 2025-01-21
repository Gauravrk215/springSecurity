package com.realnet.rb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RN_RB_DATE_PARAM_T")
public class Rn_Rb_Date_String
{
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DATE_ID")
	private int date_id;
	
	@Column(name = "REPORT_ID")
	private int report_id;
	
	@Column(name = "COL_TABLE_ALIES_NAME_DATE")
	private String col_table_alies_name_date;
	
	
	@Column(name = "COL_DATE_QUERY")
	private String col_date_query;
	
	
	@Column(name = "COLUMN_ALIAS_DATE_QUERY")
	private String column_alias_date_query;
	
	
	public int getDate_id() {
		return date_id;
	}


	public void setDate_id(int date_id) {
		this.date_id = date_id;
	}


	public int getReport_id() {
		return report_id;
	}


	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}


	public String getCol_table_alies_name_date() {
		return col_table_alies_name_date;
	}


	public void setCol_table_alies_name_date(String col_table_alies_name_date) {
		this.col_table_alies_name_date = col_table_alies_name_date;
	}


	public String getCol_date_query() {
		return col_date_query;
	}


	public void setCol_date_query(String col_date_query) {
		this.col_date_query = col_date_query;
	}


	public String getColumn_alias_date_query() {
		return column_alias_date_query;
	}


	public void setColumn_alias_date_query(String column_alias_date_query) {
		this.column_alias_date_query = column_alias_date_query;
	}


	
	
	
	
	
	
	
	

	
}

