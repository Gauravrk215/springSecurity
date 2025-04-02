package com.realnet.FabricIcardLines.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.realnet.FabricIcard.Entity.FabricIcard;

import lombok.Data;

@Entity
@Data
public class FabricIcardLines {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String header_id;
	private String file_path;
	private String xml_file_name;
	private String json_file_name;

	@Lob
	private String mapping_model;

	@Lob
	private String Model;

	@Lob
	private String layoutModel;

	@JsonBackReference
	@ManyToOne
	private FabricIcard layoutBuilder_t;

}