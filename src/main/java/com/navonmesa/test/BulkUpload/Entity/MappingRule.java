package com.realnet.BulkUpload.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class MappingRule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String entity_name;
	 private String description;
	 private String mapping_rule;
	 private boolean active;
	 private String fileType;

}
