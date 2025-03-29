package com.realnet.FabricIcard.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.realnet.FabricIcard.Entity.FabricIcard;
import com.realnet.FabricIcard.Services.FabricIcardService;

@RequestMapping(value = "/FabricIcard")
@RestController
public class FabricIcardController {

	@Autowired
	private FabricIcardService Service;

	@PostMapping("/FabricIcard")
	public FabricIcard Savedata(@RequestBody FabricIcard data) {
		FabricIcard save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/FabricIcard")
	public List<FabricIcard> getdetails() {
		List<FabricIcard> get = Service.getdetails();
		return get;
	}

	@GetMapping("/FabricIcard/{id}")
	public FabricIcard getdetailsbyId(@PathVariable Long id) {
		FabricIcard get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/FabricIcard/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/FabricIcard/{id}")
	public FabricIcard update(@RequestBody FabricIcard data, @PathVariable Long id) {
		FabricIcard update = Service.update(data, id);
		return update;
	}
}