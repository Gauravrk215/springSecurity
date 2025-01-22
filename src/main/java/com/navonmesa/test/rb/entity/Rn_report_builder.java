package com.realnet.rb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.realnet.fnd.entity.Rn_Who_Columns;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = { "module" })
@Entity
@Table(name = "RN_RB_REPORTS_T")
public class Rn_report_builder extends Rn_Who_Columns {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

//	
//	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
//	@JoinColumn(name = "MODULE_ID",insertable = false, updatable = false)
//	@JsonBackReference
//	private Rn_Module_Setup module;
//	
//	

	@Column(name = "REPORT_NAME")
	private String report_name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "REPORT_TAGS")
	private String report_tags;

	@Column(name = "DATE_STRING")
	private String date_string;

	@Column(name = "ADD_PARAM_STRING")
	private String add_param_string;

	@Column(name = "MASTER_SELECT")
	private String master_select;

	@Column(name = "GRID_HEADERS")
	private String grid_headers;

	@Column(name = "STD_PARAM_VIEW")
	private String std_param_view;

	@Column(name = "GRID_VALUES")
	private String grid_values;

	@Column(name = "MODEL_STRING")
	private String model_string;

	@Column(name = "MODULE_ID")
	private int module_id;

	@Column(name = "uiname")
	private String uiname;

	@Column(name = "ServiceName")
	private String servicename;

	@Column(name = "ReportType")
	private String reporttype;

	@Column(name = "PROJECT_ID")
	private int project_id;

	@Column(name = "IS_BUILD")
	private String is_build;

	@Column(name = "IS_UPDATED")
	private String is_updated;

	/*
	 * @OneToMany(mappedBy = "rn_report_builder", cascade = CascadeType.ALL)
	 * 
	 * @JsonManagedReference private List<Rn_rb_Tables> components;
	 * 
	 * 
	 * public List<Rn_rb_Tables> getComponents() { return components; }
	 * 
	 * public void setComponents(List<Rn_rb_Tables> components) { this.components =
	 * components; }
	 */

}
