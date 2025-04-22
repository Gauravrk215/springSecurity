package com.realnet.Gaurav_testing.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.Gaurav_testing.Entity.Gaurav_testing_t;
import com.realnet.Gaurav_testing.Services.Gaurav_testing_Service;

@RequestMapping(value = "/Gaurav_testing")
@RestController
public class Gaurav_testing_Controller {

	@Autowired
	private Gaurav_testing_Service Service;

	@PostMapping("/Gaurav_testing")
	public Gaurav_testing_t Savedata(@RequestBody Gaurav_testing_t data) {
		Gaurav_testing_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/Gaurav_testing_custom")
	public Gaurav_testing_t getdetail() {
		Gaurav_testing_t get = Service.getdetailcustom();
		return get;

	}

	@PostMapping("/Gaurav_testing_custom")
	public Gaurav_testing_t Savecustom(@RequestBody Gaurav_testing_t data) {
		Gaurav_testing_t save = Service.savecustom(data);
		return save;
	}

	@GetMapping("/Gaurav_testing")
	public List<Gaurav_testing_t> getdetails() {
		List<Gaurav_testing_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Gaurav_testing/{id}")
	public Gaurav_testing_t getdetailsbyId(@PathVariable Integer id) {
		Gaurav_testing_t get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/Gaurav_testing/{id}")
	public void delete_by_id(@PathVariable Integer id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/Gaurav_testing/{id}")
	public Gaurav_testing_t update(@RequestBody Gaurav_testing_t data, @PathVariable Integer id) {
		Gaurav_testing_t update = Service.update(data, id);
		return update;
	}
}