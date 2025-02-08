package com.realnet.template.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.realnet.WhoColumn.Entity.Who_column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TemplateFileUpload extends Who_column {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long user_id;
	private String file_name;
	private String file_changed_name;
	private String file_location;
	private String file_type;
	private Integer status;
	private String entity_name;
	private boolean isProcessed;

	private String DownloadfileName;
	private String DownloadfileLocation;
	
	private String name;
}
