package com.realnet.Gaurav_testing.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.realnet.WhoColumn.Entity.Who_column;

import lombok.Data;

@Entity
@Data
public class Gaurav_testing_t extends Who_column {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String email;
	private String mobno;
	private String address;
	private String pincode;
	private String description;

}