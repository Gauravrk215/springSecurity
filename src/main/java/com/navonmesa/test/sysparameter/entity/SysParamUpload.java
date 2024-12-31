package com.realnet.sysparameter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;



import lombok.Data;

@Entity
@Data
public class SysParamUpload {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int attachmentId;
	
	@Lob
	@Column(nullable = true)
	private byte[] attachment;
	
	@Column(nullable = true)
	private String attachmentType;
	
	@Column(nullable = true)
	private String attachmentFilename;
	
	@Column(nullable = false)
	private String cancelStatus="N";
	
//	private String createdBy;
	
	@Column(nullable = true)
	private String updatedBy;
	
	private String externalFlag;
	
	@ManyToOne
	private SysParamEntity sysParamEntity;
}
