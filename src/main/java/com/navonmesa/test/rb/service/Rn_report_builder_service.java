package com.realnet.rb.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.rb.entity.Rn_reportDTO;
import com.realnet.rb.entity.Rn_report_builder;


public interface Rn_report_builder_service {

	Page<Rn_report_builder> getAll(Pageable p);
	Rn_report_builder save(Rn_report_builder rn_report_builder);
	boolean saveReport(Rn_reportDTO report, int moduleId);
	Rn_report_builder saveReport(Rn_report_builder report, int moduleId);
	Rn_report_builder saveReportservice(Rn_report_builder report, int moduleId);
	Rn_report_builder updateById(int id, String date_string);
	public Rn_report_builder updateByIdAdhoc(int id, String date_string);
	public Rn_report_builder updateByIdGridHeaders(int id, String date_string);
	public Rn_report_builder updateByIdQuery(int id, Rn_report_builder rn_report);
	Rn_report_builder getById(int id);
	List<Rn_report_builder> getByReportId(int id);
	
	public Rn_report_builder updatereport(int reportid,Rn_report_builder reportdata);
	
	List<Map<String, Object>> getQueryData(String query);
	
	

}
