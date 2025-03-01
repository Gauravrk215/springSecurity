package com.realnet.WhoColumn.Entity;


import lombok.*;
import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Extension extends Who_column {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String extn1;
	private String extn2;
	private String extn3;
	private String extn4;
	private String extn5;
	private String extn6;
	private String extn7;
	private String extn8;
	private String extn9;
	private String extn10;
	private String extn11;
	private String extn12;
	private String extn13;
	private String extn14;
	private String extn15;

}