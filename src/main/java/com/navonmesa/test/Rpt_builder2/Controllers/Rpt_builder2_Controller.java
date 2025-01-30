package com.realnet.Rpt_builder2.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.realnet.Rpt_builder2.Entity.Rpt_builder2_t;
import com.realnet.Rpt_builder2.Services.Rpt_builder2_Service;

@RequestMapping(value = "/Rpt_builder2")
@RestController
public class Rpt_builder2_Controller {

	@Autowired
	private Rpt_builder2_Service Service;

	@PostMapping("/Rpt_builder2")
	public Rpt_builder2_t Savedata(@RequestBody Rpt_builder2_t data) {
		Rpt_builder2_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/Rpt_builder2")
	public List<Rpt_builder2_t> getdetails() {
		List<Rpt_builder2_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Rpt_builder2/{id}")
	public Rpt_builder2_t getdetailsbyId(@PathVariable Long id) {
		Rpt_builder2_t get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/Rpt_builder2/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/Rpt_builder2/{id}")
	public Rpt_builder2_t update(@RequestBody Rpt_builder2_t data, @PathVariable Long id) {
		Rpt_builder2_t update = Service.update(data, id);
		return update;
	}
}