package com.realnet.rb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.realnet.fnd.entity.Rn_Forms_Setup;

@Entity
@Table(name = "RN_RB_TABLES_T")
public class Rn_rb_Tables {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TABLES_ID")
	private int tables_id;

	public int getTables_id() {
		return tables_id;
	}

	public void setTables_id(int tables_id) {
		this.tables_id = tables_id;
	}

	
	 public int getReport_id() {
		return report_id;
	}

	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}


	@Column(name = "REPORT_ID")
	 private int report_id;
	 

	@Column(name = "TABLE_NAME")
	private String table_name;

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	@Column(name = "TABLE_ALLIAS_NAME")
	private String table_allias_name;

	public String getTable_allias_name() {
		return table_allias_name;
	}

	public void setTable_allias_name(String table_allias_name) {
		this.table_allias_name = table_allias_name;
	}

	/*
	 * @ManyToOne(fetch = FetchType.LAZY, optional = false)
	 * 
	 * @JoinColumn(name = "", nullable = false)
	 * 
	 * @JsonBackReference private RnDtoTables dtoTables;
	 */

}
