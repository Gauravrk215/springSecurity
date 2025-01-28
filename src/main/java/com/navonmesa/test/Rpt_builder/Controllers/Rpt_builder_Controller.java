package com.realnet.Rpt_builder.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.realnet.Rpt_builder.Entity.Rpt_builder_t;
import com.realnet.Rpt_builder.Services.Rpt_builder_Service;

@RequestMapping(value = "/Rpt_builder")
@RestController
public class Rpt_builder_Controller {

	@Autowired
	private Rpt_builder_Service Service;

	@PostMapping("/Rpt_builder")
	public Rpt_builder_t Savedata(@RequestBody Rpt_builder_t data) {
		Rpt_builder_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/Rpt_builder")
	public List<Rpt_builder_t> getdetails() {
		List<Rpt_builder_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Rpt_builder/{id}")
	public Rpt_builder_t getdetailsbyId(@PathVariable Long id) {
		Rpt_builder_t get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/Rpt_builder/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/Rpt_builder/{id}")
	public Rpt_builder_t update(@RequestBody Rpt_builder_t data, @PathVariable Long id) {
		Rpt_builder_t update = Service.update(data, id);
		return update;
	}
}