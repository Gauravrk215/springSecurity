package com.realnet.SequenceGenerator.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Sequence {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private int stating_no;
//
//	private int current_no;
//
//	private String date_format;
//
//	private String sequence_size;
//
//	private String in_prefix;
//	private String pr_prefix;
//
//	private String seperator;
//
//	private String cr_prefix;
//
//	private String sequence_name;
//
//	private String sequence_code;
//
//	private String implementation;
//
//	private String suffix;
//	private String demonstration;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String sequence_name;
	private Integer sequence_size;
	private Integer starting_no;
	private Integer current_no;
	private String prefix;
	private String seperator;
	private String sequence_code;
	private String suffix;

	private String demonstration;

	public Sequence(Long id, String sequence_name, Integer sequence_size, Integer stating_no, Integer current_no, String prefix, String seperator, String sequence_code, String suffix, String demonstration) {
		this.id = id;
		this.sequence_name = sequence_name;
		this.sequence_size = sequence_size;
		this.starting_no = stating_no;
		this.current_no = current_no;
		this.prefix = prefix;
		this.seperator = seperator;
		this.sequence_code = sequence_code;
		this.suffix = suffix;
		this.demonstration = demonstration;
	}

	public Sequence() {
		this.sequence_name = "RIS-Seq";
		this.sequence_size = 5;
		this.starting_no = 1;
		this.current_no = 1;
		this.prefix = "RIS";
		this.seperator = "/";
	}

}
