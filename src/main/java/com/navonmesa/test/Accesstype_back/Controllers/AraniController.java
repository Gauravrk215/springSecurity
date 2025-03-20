package com.realnet.Accesstype_back.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.Accesstype_back.Entity.Arani;
import com.realnet.Accesstype_back.Services.AraniService;

@RestController
public class AraniController {
	
	@Autowired
	private AraniService service;
	
	@GetMapping("/Arani")
	public List<Arani> getAlldetails(){
		List<Arani> get = service.getAll();
		return get;
	}

	@GetMapping("/Arani/{id}")
	public Arani getAlldetails(@PathVariable Long id) {
		Arani get = service.getdetailsbyid(id);
		return get;	
	}
	
	@PostMapping("/Arani")
	public Arani saverani(@RequestBody Arani rani) {
		Arani saverani = service.saverani(rani);
		return saverani;
	}
	
	@DeleteMapping("/Arani/{id}")
	public void delete_by_id(@PathVariable Long id) {
		service.delete_by_id(id);
	}
	
	@PutMapping("/Arani/{id}")
	public Arani update(@RequestBody Arani data, @PathVariable Long id) {
		Arani update = service.update(data, id);
		return update;
	}
}
