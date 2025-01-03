package com.realnet.BulkUpload.Controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;
import com.realnet.BulkUpload.Entity.BulkUpload_t;
import com.realnet.BulkUpload.Services.BulkUpload_Service;
@RequestMapping(value = "/BulkUpload")
@RestController
public class BulkUpload_Controller {
	
	@Autowired
	private BulkUpload_Service Service;

	@PostMapping("/BulkUpload")
	  public BulkUpload_t Savedata(@RequestBody BulkUpload_t data) {
		BulkUpload_t save = Service.Savedata(data)	;
		 return save;
	  }
		 	
	@GetMapping("/BulkUpload")
	public List<BulkUpload_t> getdetails() {
		 List<BulkUpload_t> get = Service.getdetails();		
		return get;
}

@GetMapping("/BulkUpload/{id}")
	public  BulkUpload_t  getdetailsbyId(@PathVariable Long id ) {
		BulkUpload_t  get = Service.getdetailsbyId(id);
		return get;

	}
@DeleteMapping("/BulkUpload/{id}")
	public  void delete_by_id(@PathVariable Long id ) {
	Service.delete_by_id(id);
		
	}

@PutMapping("/BulkUpload/{id}")
	public  BulkUpload_t update(@RequestBody BulkUpload_t data,@PathVariable Long id ) {
		BulkUpload_t update = Service.update(data,id);
		return update;
	}
}