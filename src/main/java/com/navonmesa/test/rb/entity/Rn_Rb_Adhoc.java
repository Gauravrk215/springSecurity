package com.realnet.rb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RN_RB_ADHOCK_PARAM_T")

public class Rn_Rb_Adhoc {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "REPORT_ID")
	private int report_id;

	@Column(name = "TABLE_ALLIAS_NAME")
	private String table_allias_name;

	@Column(name = "COLUMN_NAME")
	private String column_name;

	@Column(name = "COLUMN_ALLIAS_NAME")
	private String column_allias_name;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReport_id() {
		return report_id;
	}

	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}

	public String getTable_allias_name() {
		return table_allias_name;
	}

	public void setTable_allias_name(String table_allias_name) {
		this.table_allias_name = table_allias_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getColumn_allias_name() {
		return column_allias_name;
	}

	public void setColumn_allias_name(String column_allias_name) {
		this.column_allias_name = column_allias_name;
	}

	

}
