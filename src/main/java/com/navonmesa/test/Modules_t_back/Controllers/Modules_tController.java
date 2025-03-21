package com.realnet.Modules_t_back.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.realnet.Modules_t_back.Entity.Modules_t;
import com.realnet.Modules_t_back.Services.Modules_tService;

@RequestMapping(value = "/_back")
@RestController
public class Modules_tController {

	@Autowired
	private Modules_tService Service;

	@PostMapping("/Modules_t")

	public Modules_t Savedata(@RequestBody Modules_t data) {
		Modules_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/Modules_t")
	public List<Modules_t> getdetails() {
		List<Modules_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Modules_t/{id}")
	public Modules_t getdetailsbyId(@PathVariable Long id) {
		Modules_t get = Service.getdetailsbyId(id);
		return get;
	}

	@DeleteMapping("/Modules_t/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/Modules_t/{id}")
	public Modules_t update(@RequestBody Modules_t data, @PathVariable Long id) {
		Modules_t update = Service.update(data, id);
		return update;
	}
}