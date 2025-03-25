package com.realnet.Dashboard1.Controller;

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

import com.realnet.Dashboard1.Entity.Dashbord1_Line;
import com.realnet.Dashboard1.Entity.Dashbord_Header;
import com.realnet.Dashboard1.Repository.HeaderRepository;
import com.realnet.Dashboard1.Service.HeaderService;

@RestController
public class Dashbord1Controller {

	@Autowired
	private HeaderService headerService;

	@Autowired
	private HeaderRepository headerRepository;

//	@PreAuthorize("hasAnyRole('SYSADMIN','ProjectManager','Developer')")
	@PostMapping("/Savedata")
	public Dashbord_Header Savedata(@RequestBody Dashbord_Header dashbord_Header) {
		Dashbord_Header dash = headerService.Savedata(dashbord_Header);
		return dash;
	}

	@GetMapping("/get_Dashboard_header")
	public List<Dashbord_Header> getdetails() {
		List<Dashbord_Header> dash = headerService.getdetails();
		return dash;
	}

	@GetMapping("/get_all_lines")
	public List<Dashbord1_Line> get_all_lines() {
		List<Dashbord1_Line> dash = headerService.get_all_lines();
		return dash;
	}



	@GetMapping("/get_module_id")
	public List<Dashbord_Header> get_by_module_id(@RequestParam(value = "module_id") int module_id) {

		List<Dashbord_Header> module = headerService.get_by_module_id(module_id);
		return module;

	}

	@GetMapping("/get_dashboard_headerbyid/{id}")
	public Dashbord_Header getdetailsbyId(@PathVariable int id) {
		Dashbord_Header dash = headerService.getdetailsbyId(id);
		return dash;
	}

//	@PreAuthorize("hasAnyRole('SYSADMIN','ProjectManager','Developer')")
	@PutMapping("/update_dashboard_header")
	public Dashbord_Header update_dashboard_header(@RequestBody Dashbord_Header dashbord_Header) {
		Dashbord_Header dash = headerService.update_dashboard_header(dashbord_Header);
		return dash;
	}

//	update dashboard line by id

	@PutMapping("/update_Dashbord1_Lineby_id/{id}")
	public Dashbord1_Line update_Dashbord1_Lineby_id(@PathVariable int id, @RequestBody Dashbord1_Line dashbord1_Line) {

		Dashbord1_Line dash = headerService.update_Dashbord1_Lineby_id(id, dashbord1_Line);
		return dash;

	}

	@PostMapping("/update_Dashbord1_Line")
	public Dashbord1_Line update_Dashbord1_Line(@RequestBody Dashbord1_Line dashbord1_Line) {
		Dashbord1_Line dash1 = headerService.update_Dashbord1_Line(dashbord1_Line);
		return dash1;
	}

//	@PreAuthorize("hasAnyRole('SYSADMIN','ProjectManager','Developer')")
	@DeleteMapping("/delete_by_header_id/{id}")
	public void delete_by_id(@PathVariable int id) {
		headerService.delete_by_id(id);

	}

	// COUNT OF LIST BUILDER
	@GetMapping("/get_dashboard/{module_id}")
	public ResponseEntity<?> getREPORT(@PathVariable Integer module_id) {
		String count_wireframe = headerRepository.count_dashboardheader(module_id);

		if (count_wireframe.isEmpty()) {
			return new ResponseEntity<>(0, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(count_wireframe, HttpStatus.OK);

		}
	}

}
