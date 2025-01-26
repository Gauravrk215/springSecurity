package com.realnet.realm.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.realnet.WhoColumn.Entity.Who_column;

import lombok.Data;

@Entity
@Data
public class Realm extends Who_column {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String realm_name;

	private Long touser_id;
	private Long fromuser_id;

	private Integer realm_id;
	private String totable;

}
