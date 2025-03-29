package com.realnet.dashboard_builder_authsec.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.realnet.WhoColumn.Entity.Extension;

import lombok.Data;

@Entity
@Data
public class Dashboard extends Extension {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(length = 5000)
	private String model;

	private boolean isdashboard;

}
