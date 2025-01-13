package com.realnet.Billing.Entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.realnet.WhoColumn.Entity.Who_column;

import lombok.Data;

@Entity
@Data
public class CustomerRates_t extends Who_column {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String rateCard;
	private String destination;
	private String numericPrefix;
	private String sellRate;
	private String blockMinDuration;
	private String initBlockRate;
	private String dateStart;
	private String dateEnd;
	private String enabled;
	private String dateAdded;
	private String dateModified;

}