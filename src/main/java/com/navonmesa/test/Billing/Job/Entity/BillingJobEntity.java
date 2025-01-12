package com.realnet.Billing.Job.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;


@Data
@Entity
public class BillingJobEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String parameters;
	private String url;
	private String method;
	private String connection_name;
	private String job_type;
	private String ref;

}
