package com.realnet.FabricIcardLines.Controllers;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.realnet.FabricIcardLines.Entity.Dummyfabric;
import com.realnet.FabricIcardLines.Entity.FabricIcardLines;
import com.realnet.FabricIcardLines.Services.FabricIcardLinesService;

@RequestMapping(value = "/FabricIcardLines")
@RestController
public class FabricIcardLinesController {

	@Autowired
	private FabricIcardLinesService Service;

	@PostMapping("/FabricIcardLines")
	public FabricIcardLines Savedata(@RequestBody FabricIcardLines data) {
		FabricIcardLines save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/FabricIcardLines")
	public List<FabricIcardLines> getdetails() {
		List<FabricIcardLines> get = Service.getdetails();
		return get;
	}

	@GetMapping("/FabricIcardLines/{id}")
	public FabricIcardLines getdetailsbyId(@PathVariable Long id) {
		FabricIcardLines get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/FabricIcardLines/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/FabricIcardLines/{id}")
	public FabricIcardLines update(@RequestBody FabricIcardLines data, @PathVariable Long id) {
		FabricIcardLines update = Service.update(data, id);
		return update;
	}

//	make xml file
	@PutMapping("/FabricIcardLines/xml")
	public FabricIcardLines makexmlfile(@RequestParam String id, @RequestBody Dummyfabric dummy) throws IOException {

		String xml = dummy.getXml();
		String json = dummy.getJson();

		FabricIcardLines update = Service.makexmlfile(id, xml, json);
		return update;
	}

//	get by headerid
	@GetMapping("/FabricIcardLines/headerId/{id}")
	public FabricIcardLines getdetailsbyheaderId(@PathVariable String id) {
		FabricIcardLines get = Service.getbyheaderid(id);
		return get;

	}

//	file read
	@GetMapping("/FabricIcardLines/json/{id}")
	public String readfile(@PathVariable String id) throws IOException {
		String get = Service.readjson(id);

		return get;

	}

}