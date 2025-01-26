package com.realnet.realm.Controllers;

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

import com.realnet.realm.Entity.Realm;
import com.realnet.realm.Services.RealmService;

@RequestMapping(value = "/Realm")
//@CrossOrigin("*")
@RestController
public class RealmController {
	@Autowired
	private RealmService Service;

	@Value("${projectPath}")
	private String projectPath;

	@PostMapping("/Realm")
	public Realm Savedata(@RequestBody Realm data) {
		Realm save = Service.Savedata(data);

		return save;
	}

	@PutMapping("/Realm/{id}")
	public Realm update(@RequestBody Realm data, @PathVariable Long id) {
		Realm update = Service.update(data, id);
		return update;
	}

//	get all with pagination
	@GetMapping("/Realm/getall/page")
	public Page<Realm> getall(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Realm> get = Service.getAllWithPagination(paging);

		return get;

	}

	@GetMapping("/Realm")
	public List<Realm> getdetails() {
		List<Realm> get = Service.getdetails();
		return get;
	}

	@GetMapping("/Realm/{id}")
	public Realm getdetailsbyId(@PathVariable Long id) {
		Realm get = Service.getdetailsbyId(id);
		return get;
	}

	@DeleteMapping("/Realm/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

}