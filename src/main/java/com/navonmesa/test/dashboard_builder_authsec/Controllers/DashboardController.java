package com.realnet.dashboard_builder_authsec.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.dashboard_builder_authsec.Entity.Dashboard;
import com.realnet.dashboard_builder_authsec.Services.DashboardService;

@RequestMapping(value = "/Dashboard")
// @CrossOrigin("*") 
@RestController
public class DashboardController {
	@Autowired
	private DashboardService Service;

	@Value("${projectPath}")
	private String projectPath;

	public final String UPLOAD_DIREC = "/Files";

	@PostMapping("/Dashboard")
	public Dashboard Savedata(@RequestBody Dashboard data) {
		Dashboard save = Service.Savedata(data);

		System.out.println(save);
		return save;
	}

	@PutMapping("/Dashboard/{id}")
	public Dashboard update(@RequestBody Dashboard data, @PathVariable Integer id) {
		Dashboard update = Service.update(data, id);
		return update;
	}

	@GetMapping("/Dashboard")
	public List<Dashboard> getdetails() {
		List<Dashboard> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Dashboard/{id}")
	public Dashboard getdetailsbyId(@PathVariable Integer id) {
		Dashboard get = Service.getdetailsbyId(id);
		return get;
	}

	@DeleteMapping("/Dashboard/{id}")
	public void delete_by_id(@PathVariable Integer id) {
		Service.delete_by_id(id);

	}

}