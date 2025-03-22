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

import com.realnet.api_registery.Entity.Api_registery_line;
import com.realnet.api_registery.Services.Api_registery_lineService;

@RequestMapping(value = "/Api_registery_line")
//@CrossOrigin("*")
@RestController
public class Api_registery_lineController {
	@Autowired
	private Api_registery_lineService Service;

	@Value("${projectPath}")
	private String projectPath;

	@PostMapping("/Api_registery_line")
	public Api_registery_line Savedata(@RequestBody Api_registery_line data) {
		Api_registery_line save = Service.Savedata(data);

		return save;
	}

	@PutMapping("/Api_registery_line/{id}")
	public Api_registery_line update(@RequestBody Api_registery_line data, @PathVariable Integer id) {
		Api_registery_line update = Service.update(data, id);
		return update;
	}

//	get all with pagination
	@GetMapping("/Api_registery_line/getall/page")
	public Page<Api_registery_line> getall(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Api_registery_line> get = Service.getAllWithPagination(paging);

		return get;

	}

	@GetMapping("/Api_registery_line")
	public List<Api_registery_line> getdetails() {
		List<Api_registery_line> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Api_registery_line/{id}")
	public Api_registery_line getdetailsbyId(@PathVariable Integer id) {
		Api_registery_line get = Service.getdetailsbyId(id);
		return get;
	}

	@DeleteMapping("/Api_registery_line/{id}")
	public void delete_by_id(@PathVariable Integer id) {
		Service.delete_by_id(id);

	}

//	get all by header id
	@GetMapping("/header/Api_registery_line/{header_id}")
	public List<Api_registery_line> getallbyheaderId(@PathVariable Long header_id) {
		List<Api_registery_line> get = Service.getLinesbyHeaderid(header_id);
		return get;
	}

}