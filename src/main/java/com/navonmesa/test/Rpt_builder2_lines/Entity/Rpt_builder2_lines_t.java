package com.realnet.Rpt_builder2_lines.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.realnet.Rpt_builder2.Entity.Rpt_builder2_t;

import lombok.Data;

@Entity
@Data
public class Rpt_builder2_lines_t {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String header_id;
	@Lob
	private String model;

	@JsonBackReference
	@ManyToOne
	private Rpt_builder2_t  rpt_builder2_t;

}