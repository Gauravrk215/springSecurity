package com.realnet.sqlworkbech.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class SqlModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String conn_string;
	private String username;
	private String password;
	private String driver_class_name;
	
	private String name;
	private String type;
	private String description;
	private boolean active;

	

}
