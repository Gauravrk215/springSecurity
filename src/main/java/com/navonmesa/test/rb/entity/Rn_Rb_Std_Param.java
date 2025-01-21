package com.realnet.rb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RN_RB_STD_PARAM_T")
public class Rn_Rb_Std_Param
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STD_ID")
	private int std_id;
	
	
	@Column(name = "REPORT_ID")
	private int report_id;
	
	@Column(name = "col_table_alies_name_std_para")
	private String col_table_alies_name_std_para;
	
	
	@Column(name = "col_std_para_query")
	private String col_std_para_query;
	
	@Column(name = "FIELD_TYPE")
	private String field_type;
	
	
	@Column(name = "SP_FOR_DD")
	private String sp_for_dd;
	
	
	
	
	
	public int getStd_id() {
		return std_id;
	}

	public void setStd_id(int std_id) {
		this.std_id = std_id;
	}

	public int getReport_id() {
		return report_id;
	}

	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}

	public String getCol_table_alies_name_std_para() {
		return col_table_alies_name_std_para;
	}

	public void setCol_table_alies_name_std_para(String col_table_alies_name_std_para) {
		this.col_table_alies_name_std_para = col_table_alies_name_std_para;
	}

	public String getCol_std_para_query() {
		return col_std_para_query;
	}

	public void setCol_std_para_query(String col_std_para_query) {
		this.col_std_para_query = col_std_para_query;
	}

	public String getField_type() {
		return field_type;
	}

	public void setField_type(String field_type) {
		this.field_type = field_type;
	}

	

	public String getSp_for_dd() {
		return sp_for_dd;
	}

	public void setSp_for_dd(String sp_for_dd) {
		this.sp_for_dd = sp_for_dd;
	}
	
	
	
	
	
	
	
	

	
}

