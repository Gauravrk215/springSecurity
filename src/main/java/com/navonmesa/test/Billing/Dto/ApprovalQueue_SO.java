package com.realnet.Billing.Dto;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
public class ApprovalQueue_SO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String formCode;
	private String documentSeq;
	private String approver;
	private String actionType;
	private String actionTaken;
	private String comments;
	private Date actionedAt;

	private String tablename;

	@OneToMany(mappedBy = "approvalQueueSO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<ApprovalNote_SO> history;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "service_order_id")
//    @JsonBackReference
	private Long service_order_id;

}
