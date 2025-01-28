package com.realnet.Rpt_builder.Entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Rpt_builder_t {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String folder;
	private String query;
	private boolean date_param_flag;
	private boolean adhoc_param_flag;
	private String adhoc_param_string;
	private String Std_param_json;

}