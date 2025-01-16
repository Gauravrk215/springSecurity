package com.realnet.event_management.Controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.realnet.config.EmailService;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.*;
import com.realnet.event_management.Entity.Event_Management;
import com.realnet.event_management.Services.Event_ManagementService ;
@RequestMapping(value = "/Event_Management")
 @CrossOrigin("*") 
@RestController
public class Event_ManagementController {
	@Autowired
	private Event_ManagementService Service;

@Value("${projectPath}")
	private String projectPath;
 














	@PostMapping("/Event_Management")
		  public Event_Management Savedata(@RequestBody Event_Management data) {
		Event_Management save = Service.Savedata(data)	;














 return save;
	  }
@PutMapping("/Event_Management/{id}")
	public  Event_Management update(@RequestBody Event_Management data,@PathVariable Integer id ) {
		Event_Management update = Service.update(data,id);
		return update;
	}	 
//	get all with pagination
	@GetMapping("/Event_Management/getall/page")
	public Page<Event_Management> getall(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Event_Management> get = Service.getAllWithPagination(paging);

		return get;

	}	
	@GetMapping("/Event_Management")
	public List<Event_Management> getdetails() {
		 List<Event_Management> get = Service.getdetails();		
		return get;
}
// get all without authentication 

	@GetMapping("/token/Event_Management")
	public List<Event_Management> getallwioutsec() {
		 List<Event_Management> get = Service.getdetails();		
		return get;
}
@GetMapping("/Event_Management/{id}")
	public  Event_Management  getdetailsbyId(@PathVariable Integer id ) {
		Event_Management  get = Service.getdetailsbyId(id);
		return get;
	}
@DeleteMapping("/Event_Management/{id}")
	public  void delete_by_id(@PathVariable Integer id ) {
	Service.delete_by_id(id);
		
	}

}