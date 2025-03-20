package com.realnet.Accesstype_back.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.Accesstype_back.Entity.Agyana;
import com.realnet.Accesstype_back.Repository.AgyanaRepository;


@RequestMapping(value = "/token/access_type")
@RestController
public class AgyanaController {

	@Autowired
	private AgyanaRepository agyanaRepository;
	
	@GetMapping("/agyana")
	public List<Agyana> getAlldetails() {
		List<Agyana> get = getdetails();
		return get;
	}
	
	public List<Agyana> getdetails() {
		return (List<Agyana>) agyanaRepository.findAll();
	}
}
