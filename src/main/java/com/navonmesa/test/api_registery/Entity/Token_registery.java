package com.realnet.api_registery.Entity;

import lombok.*;

import javax.persistence.*;

import com.realnet.WhoColumn.Entity.Extension;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Token_registery extends Extension {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String token_name;

	@Lob
	private String token;

}
