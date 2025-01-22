package com.realnet.rb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RN_RB_WHERE_PARAM_T")
public class Rn_Rb_Where_Param
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WHERE_ID")
	private int where_id;
	
	@Column(name = "REPORT_ID")
	private int report_id;
	
	@Column(name = "EXPLECITY")
	private String explecity;
	
	@Column(name = "WHERE_COLOUMN1_TBL_ALIAS_NAME")
	private String where_coloumn1_tbl_alias_name;
	
	@Column(name = "WHERE_COLOUMN")
	private String where_coloumn;
	
	@Column(name = "WHERE_CONDITION")
	private String where_condition;
	
	@Column(name = "SWITCH_CONTROL")
	private String switch_control;
	
	@Column(name = "WHERE_COLOUMN2_TBL_ALIAS_NAME")
	private String where_coloumn2_tbl_alias_name;
	
	@Column(name = "WHERE_COLOUMN2")
	private String where_coloumn2;
	
	
	
	public int getWhere_id() {
		return where_id;
	}

	public void setWhere_id(int where_id) {
		this.where_id = where_id;
	}

	public int getReport_id() {
		return report_id;
	}

	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}

	public String getExplecity() {
		return explecity;
	}

	public void setExplecity(String explecity) {
		this.explecity = explecity;
	}

	public String getWhere_coloumn1_tbl_alias_name() {
		return where_coloumn1_tbl_alias_name;
	}

	public void setWhere_coloumn1_tbl_alias_name(String where_coloumn1_tbl_alias_name) {
		this.where_coloumn1_tbl_alias_name = where_coloumn1_tbl_alias_name;
	}

	public String getWhere_coloumn() {
		return where_coloumn;
	}

	public void setWhere_coloumn(String where_coloumn) {
		this.where_coloumn = where_coloumn;
	}

	public String getWhere_condition() {
		return where_condition;
	}

	public void setWhere_condition(String where_condition) {
		this.where_condition = where_condition;
	}

	public String getSwitch_control() {
		return switch_control;
	}

	public void setSwitch_control(String switch_control) {
		this.switch_control = switch_control;
	}

	public String getWhere_coloumn2_tbl_alias_name() {
		return where_coloumn2_tbl_alias_name;
	}

	public void setWhere_coloumn2_tbl_alias_name(String where_coloumn2_tbl_alias_name) {
		this.where_coloumn2_tbl_alias_name = where_coloumn2_tbl_alias_name;
	}

	public String getWhere_coloumn2() {
		return where_coloumn2;
	}

	public void setWhere_coloumn2(String where_coloumn2) {
		this.where_coloumn2 = where_coloumn2;
	}

	
	
}

