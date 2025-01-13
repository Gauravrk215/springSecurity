package com.realnet.Billing.Entitys;

import lombok.*;

import java.util.Date;

import javax.persistence.*;


@Entity
@Data
public class BillingPeriods_t {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date periodStart;

	private Date periodEnd;

	private Date dueDate;

	private String status;

}