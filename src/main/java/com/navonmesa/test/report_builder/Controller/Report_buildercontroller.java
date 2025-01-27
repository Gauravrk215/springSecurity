package com.realnet.report_builder.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.report_builder.Entity.ReportQueryDataDTO;
import com.realnet.report_builder.Entity.Rp_Line;
import com.realnet.report_builder.Entity.Rp_builder;
import com.realnet.report_builder.Repository.ReportRepository;
import com.realnet.report_builder.Service.Report_builderService;

@RestController
public class Report_buildercontroller {
	
	@Autowired
	private Report_builderService report_builderService;
	
	@Autowired
	private ReportRepository reportRepository;

	// save report builder
	@PostMapping("/Report_builder")
	
	  public Rp_builder Savedata(@RequestBody Rp_builder dashbord_Header) {
		Rp_builder dash = report_builderService.Savedata(dashbord_Header)	;
		 return dash;
	  }
		 
	// get all report builder
	@GetMapping("/Report_builder")
	public List<Rp_builder> getdetails() {
		 List<Rp_builder> dash = report_builderService.getdetails();		
		return dash;
	}
	
	// get all report line 
	@GetMapping("/Report_line")
	public List<Rp_Line> get_all_lines() {
		 List<Rp_Line> dash = report_builderService.get_all_lines();		
		return dash;
	}
	
	

	// get report builder  by module id
	@GetMapping("/get_by_module_id")
	public  List<Rp_builder> get_by_module_id(@RequestParam(value = "module_id") int module_id) {
		
		List<Rp_builder> module = report_builderService.get_by_module_id(module_id);
		return module;
		
	}
	
	// get report builder by id
	@GetMapping("/Report_builder/{id}")
	public  Rp_builder getdetailsbyId(@PathVariable int id ) {
		Rp_builder dash = report_builderService.getdetailsbyId(id);
		return dash;
	}
	
	// update report builder by id
	@PutMapping("/Report_builder/{id}")
	public  Rp_builder update_dashboard_header(@PathVariable int id,@RequestBody Rp_builder dashbord_Header) {
		Rp_builder dash = report_builderService.update_dashboard_header(id,dashbord_Header);
		return dash;
	}
	
//	update report builder line by id
	
	@PutMapping("/Report_line/{id}")
	public  Rp_Line update_Dashbord1_Lineby_id(@PathVariable int id, 
			                                          @RequestBody Rp_Line dashbord1_Line ) {

		Rp_Line dash = report_builderService.update_Dashbord1_Lineby_id(id,dashbord1_Line);
		return dash;
	
	}
	
	// save report line 
	@PostMapping("/Report_line")
	public  Rp_Line update_Dashbord1_Line(@RequestBody Rp_Line dashbord1_Line ) {
		Rp_Line dash1 = report_builderService.update_Dashbord1_Line(dashbord1_Line);
		return dash1;
	}
	
	// delete report builder by id
	@DeleteMapping("/Report_builder/{id}")
	public  void delete_by_id(@PathVariable int id ) {
		report_builderService.delete_by_id(id);
		
	}
	
//	GET NUMBERS OF IDLIST of report builder
	
	@GetMapping("/GetReportBuilderOb")
	public List<Object> getobject() {
		return this.reportRepository.findCount();
		
	}
	
	@PostMapping("/getQuery")
	public ResponseEntity<?> getQueryBydata(@RequestBody ReportQueryDataDTO data){
		List<String> tables = data.getTables();
		List<String> columns = data.getColumns();
		List<String> conditions = data.getConditions();
		
		String Query = "SELECT ";
		for(int i=0;i<columns.size();i++) {
			Query+=columns.get(i)+" as ";
			Query+=columns.get(i).substring(2);
			if(i!=columns.size()-1) {
				Query+=",";
			}
		}
		
		Query+=" FROM ";
		String prefixs = "abcdefghijklmnopqrstuvwxyz";
		
		for(int i=0;i<tables.size();i++) {
			Query+=tables.get(i)+" "+prefixs.charAt(i)+" ";
		}
		
		Query+="WHERE 1=1 ";
		
		for(int i=0;i<conditions.size();i++) {
			Query+=conditions.get(i)+" ";
		}
		
		return new ResponseEntity(Query,HttpStatus.OK);
	}

	
}
