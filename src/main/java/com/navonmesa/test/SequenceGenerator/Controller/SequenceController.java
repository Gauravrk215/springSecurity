package com.realnet.SequenceGenerator.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.SequenceGenerator.Entity.Sequence;
import com.realnet.SequenceGenerator.Service.SequenceService;
import com.realnet.SequenceGenerator.repo.SeqRepo;

@RequestMapping(value = "/sureserve/sequence")
@RestController
public class SequenceController {
//
//	@Autowired
//	private SeqRepo seqRepo;
//
////	 add data 
//	@PostMapping("/seq")
//	public ResponseEntity<?> post(@RequestBody Sequence j) {
//
//		Sequence save = seqRepo.save(j);
//		return new ResponseEntity<>(save, HttpStatus.OK);
//
//	}
//
////	 update data
//	@PutMapping("/seq/{id}")
//	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Sequence j) {
//		Sequence get = seqRepo.findById(id).get();
//
//		get.setCr_prefix(j.getCr_prefix());
//		get.setDate_format(j.getDate_format());
//		get.setIn_prefix(j.getIn_prefix());
//		get.setSequence_size(j.getSequence_size());
//		get.setPr_prefix(j.getPr_prefix());
//		get.setStating_no(j.getStating_no());
//		get.setCurrent_no(j.getCurrent_no());
//		get.setDemonstration(j.getDemonstration());
//		get.setImplementation(j.getImplementation());
//		get.setSequence_code(j.getSequence_code());
//		get.setSequence_name(j.getSequence_name());
//		get.setStating_no(j.getStating_no());
//		get.setSuffix(j.getSuffix());
//
//		Sequence save = seqRepo.save(get);
//		return new ResponseEntity<>(save, HttpStatus.OK);
//
//	}
//
//	// get by id
//	@GetMapping("/seq/{id}")
//	public ResponseEntity<?> get(@PathVariable Long id) {
//
//		Sequence get = seqRepo.findById(id).get();
//		return new ResponseEntity<>(get, HttpStatus.OK);
//
//	}
//
//	// get all
//	@GetMapping("/seq")
//	public List<Sequence> getdetails() {
//		List<Sequence> get = seqRepo.findAll();
//		return get;
//	}
//
//	// delete by id
//	@DeleteMapping("/seq/{id}")
//	public void delete_by_id(@PathVariable Long id) {
//		seqRepo.deleteById(id);
//
//	}
	
	
	
	
	
	@Autowired
	private SeqRepo seqRepo;

	@Autowired
	private SequenceService service;

	
	@PostMapping("/seq")
	public ResponseEntity<String> Savedata(@RequestBody Sequence data) {
//		AppUser loggedInUser = userService.getLoggedInUser();
//		Long userId = loggedInUser.getUserId();
//		data.setAccountId(userId);
		service.Savedata(data);
		return ResponseEntity.ok().build();
	}
	
	
	//create sequence-series
	@PostMapping("create")
	public ResponseEntity<?> createSequence(
			@RequestBody Sequence seq
	)
	{
		Sequence old = seqRepo.findByPrefixAndSuffix(seq.getPrefix(), seq.getSuffix()).orElse(null);
		if(old != null){
			String error = "\nPrefix: " + seq.getPrefix() + "\nSuffix: " + seq.getSuffix();
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Sequence already exists with "+error);
		}

		return new ResponseEntity(service.createSequence(seq), HttpStatus.CREATED);
	}

	//Testing sequence generation
	@GetMapping("/seq/test")
	public String generate(
			@RequestParam String pre,
			@RequestParam(defaultValue = "#{T(java.time.Year).now().toString()}") String suf
	)
	{
		System.out.println(suf);
		return service.GenerateSequence(pre,suf);
	}


	//update Data
	@PutMapping("/seq/{id}")
	public ResponseEntity<Sequence> updateSequence(
			@PathVariable Long id,
			@RequestBody Sequence seq
	) {
		Sequence old = seqRepo.findById(id).orElse(null);

		if (old != null) {
			old.setSequence_size(seq.getSequence_size());
			old.setStarting_no(seq.getStarting_no());
			old.setCurrent_no(seq.getCurrent_no());
			old.setDemonstration(seq.getDemonstration());
			old.setSequence_code(seq.getSequence_code());
			old.setSequence_name(seq.getSequence_name());
			old.setSuffix(seq.getSuffix());
			old.setPrefix(seq.getPrefix());
			old.setSeperator(seq.getSeperator());

			Sequence savedSequence = seqRepo.save(old);
			return new ResponseEntity<>(savedSequence, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	// get by id
	@GetMapping("/seq/{id}")
	public ResponseEntity<?> get(
			@PathVariable Long id
	) {

		Sequence get = seqRepo.findById(id).get();
		return new ResponseEntity<>(get, HttpStatus.OK);

	}

	// get all
	@GetMapping("/seq")
	public List<Sequence> getdetails() {
		List<Sequence> get = seqRepo.findAll();
		return get;
	}

	// delete by id
	@DeleteMapping("/seq/{id}")
	public void delete_by_id(@PathVariable Long id) {
		seqRepo.deleteById(id);

	}
}
