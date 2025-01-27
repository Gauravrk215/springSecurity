package com.realnet.report_builder.Entity;

import java.util.List;

import lombok.Data;

@Data
public class ReportQueryDataDTO {
	
	private List<String> tables;
	private List<String> columns;
	private List<String> conditions;

}
