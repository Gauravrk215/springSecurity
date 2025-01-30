package com.realnet.Rpt_builder2.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.realnet.Rpt_builder2_lines.Entity.Rpt_builder2_lines_t;

import lombok.Data;

@Entity
@Data
public class Rpt_builder2_t  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String reportName;
	private String description;
	private Boolean active;
	private Boolean isSql;

	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "rpt_builder2_t")
	private List<Rpt_builder2_lines_t> Rpt_builder2_lines = new ArrayList<>();
	

}