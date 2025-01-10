package com.realnet.Billing.Entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ApprovalHistory_t {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String document_type;
	private Long document_id;
	private String actioned_by;
	private String action;
	private String comments;

	private String approvalStatus;

}