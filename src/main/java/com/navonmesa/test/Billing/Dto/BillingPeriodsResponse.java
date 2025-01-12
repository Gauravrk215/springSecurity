package com.realnet.Billing.Dto;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BillingPeriodsResponse {

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date periodStart;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date periodEnd;

	// Constructors, getters, setters
	
	
	 public BillingPeriodsResponse(Date periodStart, Date periodEnd) {
	        this.periodStart = periodStart;
	        this.periodEnd = periodEnd;
	 }

}
