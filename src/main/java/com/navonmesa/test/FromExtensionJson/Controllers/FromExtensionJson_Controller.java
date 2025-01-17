package com.realnet.FromExtensionJson.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.FromExtensionJson.Entity.FromExtensionJson_t;
import com.realnet.FromExtensionJson.Repository.FromExtensionJson_Repository;
import com.realnet.FromExtensionJson.Services.FromExtensionJson_Service;
@RequestMapping(value = "/FromExtensionJson")
@RestController
public class FromExtensionJson_Controller {

	@Autowired
	private FromExtensionJson_Service Service;
	
	@Autowired
	private FromExtensionJson_Repository extensionJson_Repository;

	@PostMapping("/ExtensionJson")
	public FromExtensionJson_t Savedata(@RequestBody FromExtensionJson_t data) {
		FromExtensionJson_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/ExtensionJson")
	public List<FromExtensionJson_t> getdetails() {
		List<FromExtensionJson_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/ExtensionJson/{id}")
	public FromExtensionJson_t getdetailsbyId(@PathVariable Long id) {
		FromExtensionJson_t get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/ExtensionJson/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/ExtensionJson/{id}")
	public FromExtensionJson_t update(@RequestBody FromExtensionJson_t data, @PathVariable Long id) {
		FromExtensionJson_t update = Service.update(data, id);
		return update;
	}
	
	
	
	
	@GetMapping("/ExtensionJson/{formCode}")
	public ResponseEntity<String> getdetailsbyFormcode(@PathVariable String formCode) {
		FromExtensionJson_t get = extensionJson_Repository.getdetailsbyFormCode(formCode);
	//	return get;
		
		 if (get != null) {
		        String jsonObject = get.getJsonObject();
		        return ResponseEntity.ok().body(jsonObject);
		    } else {
		        return ResponseEntity.notFound().build();
		    }

	}

	
	@GetMapping("/ListExtensionJson/{formCode}")
	public ResponseEntity<List<String>> getJsonObjectsByFormCodeList(@PathVariable String formCode) {
	    List<String> jsonObjects = extensionJson_Repository.getJsonObjectsByFormCode(formCode);
	    if (!jsonObjects.isEmpty()) {
	        return ResponseEntity.ok().body(jsonObjects);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	

	
	

	
	
}