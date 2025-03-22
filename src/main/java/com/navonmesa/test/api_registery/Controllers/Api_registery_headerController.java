package com.realnet.api_registery.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.api_registery.Entity.Api_registery_header;
import com.realnet.api_registery.Services.Api_registery_headerService;

@RequestMapping(value = "/Api_registery_header")
//@CrossOrigin("*")
@RestController
public class Api_registery_headerController {
	@Autowired
	private Api_registery_headerService Service;

	@Value("${projectPath}")
	private String projectPath;

	@PostMapping("/Api_registery_header")
	public Api_registery_header Savedata(@RequestBody Api_registery_header data) {
		Api_registery_header save = Service.Savedata(data);

		return save;
	}

	@PutMapping("/Api_registery_header/{id}")
	public Api_registery_header update(@RequestBody Api_registery_header data, @PathVariable Long id) {
		Api_registery_header update = Service.update(data, id);
		return update;
	}

//	get all with pagination
	@GetMapping("/Api_registery_header/getall/page")
	public Page<Api_registery_header> getall(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Api_registery_header> get = Service.getAllWithPagination(paging);

		return get;

	}

	@GetMapping("/Api_registery_header")
	public List<Api_registery_header> getdetails() {
		List<Api_registery_header> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Api_registery_header/{id}")
	public Api_registery_header getdetailsbyId(@PathVariable Long id) {
		Api_registery_header get = Service.getdetailsbyId(id);
		return get;
	}

	@DeleteMapping("/Api_registery_header/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

}