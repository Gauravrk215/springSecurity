package com.realnet.rb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RN_RB_COLUMN_T")
public class Rn_rb_Column {

	

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "ID")
		private int id;
		

		@Column(name = "REPORT_ID")
		private int report_id;
		
		@Column(name = "COLUMN_NAME")
		private String column_name;

		@Column(name = "COLUMN_ALLIAS_NAME")
		private String column_allias_name;
		
		@Column(name = "ASC_DESC")
		private String asc_desc;

		@Column(name = "TABLE_ALLIES_NAME")
		private String table_allies_name;

		

		public int getReport_id() {
			return report_id;
		}

		public void setReport_id(int report_id) {
			this.report_id = report_id;
		}

		

		public String getColumn_allias_name() {
			return column_allias_name;
		}

		public void setColumn_allias_name(String column_allias_name) {
			this.column_allias_name = column_allias_name;
		}

		public String getAsc_desc() {
			return asc_desc;
		}

		public void setAsc_desc(String asc_desc) {
			this.asc_desc = asc_desc;
		}

		@Column(name = "FUNCTIONS")
		private String functions;

		public String getFunctions() {
			return functions;
		}

		public void setFunctions(String functions) {
			this.functions = functions;
		}

		

		public String getTable_allies_name() {
			return table_allies_name;
		}

		public void setTable_allies_name(String table_allies_name) {
			this.table_allies_name = table_allies_name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		

		public String getColumn_name() {
			return column_name;
		}

		public void setColumn_name(String column_name) {
			this.column_name = column_name;
		}

	}

	
	

