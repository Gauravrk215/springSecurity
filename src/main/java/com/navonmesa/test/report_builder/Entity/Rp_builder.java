package com.realnet.report_builder.Entity;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.ToString;

@ToString(exclude = {"rp_Line"})
@Entity
@Data
public class Rp_builder extends Rp_Who_collumn{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;


// ==============================
	@Column(name = "IS_BUILD")
	private boolean build;
	
	@Column(name = "MENU_NAME")
	private String menuName;

	@Column(name = "IS_UPDATED")
	private boolean updated;
	
	private String tech_Stack;
	
	private String report_builder_name;
	
	private int module_id;
	
	private String description;
	
	private String secuirity_profile;
	
	private String page_size;


	
   @JsonManagedReference
	@OneToMany(  cascade=CascadeType.ALL, mappedBy ="rp_builder"  )
	private List<Rp_Line> rp_Line = new ArrayList<>();



	
	
	

}
