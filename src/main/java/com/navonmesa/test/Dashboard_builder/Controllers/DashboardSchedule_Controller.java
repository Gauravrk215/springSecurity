package com.realnet.Dashboard_builder.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.realnet.Dashboard_builder.Entity.DashboardSchedule_t;
import com.realnet.Dashboard_builder.Services.DashboardSchedule_Service;

@RequestMapping(value = "/DashboardSchedule")
@RestController
public class DashboardSchedule_Controller {

	@Autowired
	private DashboardSchedule_Service Service;

	@PostMapping("/DashboardSchedule")
	public DashboardSchedule_t Savedata(@RequestBody DashboardSchedule_t data) {
		DashboardSchedule_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/DashboardSchedule")
	public List<DashboardSchedule_t> getdetails() {
		List<DashboardSchedule_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/DashboardSchedule/{id}")
	public DashboardSchedule_t getdetailsbyId(@PathVariable Long id) {
		DashboardSchedule_t get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/DashboardSchedule/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/DashboardSchedule/{id}")
	public DashboardSchedule_t update(@RequestBody DashboardSchedule_t data, @PathVariable Long id) {
		DashboardSchedule_t update = Service.update(data, id);
		return update;
	}
}