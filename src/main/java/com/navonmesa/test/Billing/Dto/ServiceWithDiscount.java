package com.realnet.Billing.Dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;


import lombok.Data;

@Data
public class ServiceWithDiscount {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String memo;
	private boolean active;
	private String type;
	private String period;
	private String sellPrice;
	private String selfCost;

	private String serviceCode;
	private String hsnCodes;
	private boolean exempt;
	private boolean nonGst;
	private String taxRateType;
	private String tarrifCode;
	private String hsn_sacNumber;
	private String natureOfTrans;
	private String productType;

	@Lob
	private String inputJson;
	
	
	private String disc;
	
	
	private String gst ;

}


