package com.realnet.rb.Controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.rb.repository.Rn_report_builder_repository;
import com.realnet.rb.service.Rn_report_builder_service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Rn_report_builder" })
public class servicereportcontroller {
	
	@Autowired
	Rn_report_builder_repository reportrepo;
	


	@Autowired
	private Rn_report_builder_service rn_report_builder_service;

	
	@ApiOperation(value = "Add new Report")
	@PostMapping(value = "/report-builder_service")
	public ResponseEntity<?> saveReport(@RequestParam(value = "moduleId") Integer moduleId,
			@Valid @RequestBody Rn_report_builder report) throws IOException {
		System.out.println("report controller start" + report.getReport_name()  );
		
		System.out.println("service name" + report.getServicename());
			
		
		Rn_report_builder status = rn_report_builder_service.saveReportservice(report, moduleId);
		if (status.getId()!=0) {			
			return  ResponseEntity.status(HttpStatus.OK).body(status);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you have inserted wrong data");
		}
	}
	
	
	
	@ApiOperation(value = "Update A Report", response = Rn_report_builder.class)
	@PutMapping("/updatereport/{id}")
	public ResponseEntity<Rn_report_builder> updateFunction(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") Integer id, @Valid @RequestBody Rn_report_builder report) {
		System.out.println("report id::::"+id);
		Rn_report_builder updatedFunction = rn_report_builder_service.updatereport(id, report);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedFunction);
	}
	
	
}
