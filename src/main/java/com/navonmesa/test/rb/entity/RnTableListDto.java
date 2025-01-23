package com.realnet.rb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RN_TABLE_LIST")
public class RnTableListDto {
	//private int id;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "TABLE_NAME")
	private String table_name;

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public RnTableListDto() {
		
	}

	public RnTableListDto(String table_name) {
		this.table_name = table_name;
	}
}
