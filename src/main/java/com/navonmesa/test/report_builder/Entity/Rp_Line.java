package com.realnet.report_builder.Entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.ToString;
@ToString(exclude = {"rp_builder"})
@Data
@Entity
@Table
public class Rp_Line extends Rp_Who_collumn  {

	 
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
		
		private String header_id;
		
		@Column(length = 5000)
	     private String Model;
		
		@JsonBackReference
		@ManyToOne
		private Rp_builder rp_builder;
		
	
		
}
