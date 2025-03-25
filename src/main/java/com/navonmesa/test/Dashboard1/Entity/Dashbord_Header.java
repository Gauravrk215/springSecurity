package com.realnet.Dashboard1.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.ToString;

@ToString(exclude = { "Dashbord1_Line" })
@Entity
@Data
public class Dashbord_Header extends dashbord_Who_collumn {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

// ==============================

	@Column(name = "MENU_NAME")
	private String menuName;

	@Column(name = "IS_UPDATED")
	private boolean updated;

	private String tech_Stack;

	private String object_type;
	private String sub_object_type;

	@Column(name = "IS_BUILD")
	private boolean build;

	private boolean testing;

	private String dashboard_name;

	private int module_id;

	private String description;

	private String secuirity_profile;

	private Boolean add_to_home;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dashbord_Header")
	private List<Dashbord1_Line> dashbord1_Line = new ArrayList<>();

}
