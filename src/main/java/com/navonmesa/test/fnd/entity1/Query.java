package com.realnet.fnd.entity1;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.realnet.Dashboard1.Entity.dashbord_Who_collumn;

import lombok.Data;

@Entity
@Data
public class Query extends dashbord_Who_collumn{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String queryname;
	private String sql_query;

}
