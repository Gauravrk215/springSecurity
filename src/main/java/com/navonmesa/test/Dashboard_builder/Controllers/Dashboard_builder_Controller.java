package com.realnet.Dashboard_builder.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.realnet.Dashboard_builder.Entity.Dashboard_builder_t;
import com.realnet.Dashboard_builder.Repository.Dashboard_builder_Repository;
import com.realnet.Dashboard_builder.Services.Dashboard_builder_Service;

@RequestMapping(value = "/Dashboard_builder")
@RestController
public class Dashboard_builder_Controller {

	@Autowired
	private Dashboard_builder_Service Service;
	
	
	@Autowired
	private Dashboard_builder_Repository builder_Repository;

	@PostMapping("/Dashboard_builder")
	public Dashboard_builder_t Savedata(@RequestBody Dashboard_builder_t data) {
		Dashboard_builder_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/Dashboard_builder")
	public List<Dashboard_builder_t> getdetails() {
		List<Dashboard_builder_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Dashboard_builder/{id}")
	public Dashboard_builder_t getdetailsbyId(@PathVariable Long id) {
		Dashboard_builder_t get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/Dashboard_builder/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/Dashboard_builder/{id}")
	public Dashboard_builder_t update(@RequestBody Dashboard_builder_t data, @PathVariable Long id) {
		Dashboard_builder_t update = Service.update(data, id);
		return update;
	}
	
	
	
	
}