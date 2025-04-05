package com.realnet.fnd.controller1;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.entity1.Query;
import com.realnet.fnd.repository1.QueryRepository;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;

@RequestMapping(value = "/FndQuery", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class QueryController {

	@Autowired
	private QueryRepository queryRepository;
	@Autowired
	private AppUserServiceImpl userService;

//	 ADD DATA 
	@PostMapping("/query")
	public ResponseEntity<?> add(@RequestBody Query query) {
		AppUser loggedInUser = userService.getLoggedInUser();
		query.setAccountId(loggedInUser.getAccount().getAccount_id());
		query.setCreatedBy(loggedInUser.getUserId());

		Query save = queryRepository.save(query);
		return new ResponseEntity<>(save, HttpStatus.CREATED);
	}

//	GET BY ID
	@GetMapping("/query/{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		Query query = queryRepository.findById(id).orElseThrow(null);
		return new ResponseEntity<>(query, HttpStatus.OK);

	}

//	GET ALL 
	@GetMapping("/query")
	public ResponseEntity<?> getall() {
		List<Query> query = queryRepository.findAll();
		return new ResponseEntity<>(query, HttpStatus.OK);

	}

//		UPDATE BY ID
	@PutMapping("/query/{id}")
	public ResponseEntity<?> updateMenuGRPAccess(@PathVariable Long id, @RequestBody Query q) {
		Query query = queryRepository.findById(id).orElseThrow(null);
		if (query == null) {
			throw new ResourceNotFoundException("no resource found");
		}
		query.setSql_query(q.getSql_query());
		query.setQueryname(q.getQueryname());

		Query a = queryRepository.save(query);
		return new ResponseEntity<>(a, HttpStatus.OK);
	}

//		delete BY ID
	@DeleteMapping("/query/{id}")
	public void deleteGrpMenuAccess(@PathVariable Long id) {
		Query query = queryRepository.findById(id).orElseThrow(null);
		if (Objects.isNull(query))
			throw new ResourceNotFoundException("no resource found");

		queryRepository.delete(query);
	}

}
