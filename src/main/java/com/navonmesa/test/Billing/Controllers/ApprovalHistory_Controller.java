package com.realnet.Billing.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.Billing.Entitys.ApprovalHistory_t;
import com.realnet.Billing.Services.ApprovalHistory_Service;

@RequestMapping(value = "/ApprovalHistory")
@RestController
public class ApprovalHistory_Controller {

	@Autowired
	private ApprovalHistory_Service Service;

	@PostMapping("/ApprovalHistory")
	public ApprovalHistory_t Savedata(@RequestBody ApprovalHistory_t data) {
		ApprovalHistory_t save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/ApprovalHistory")
	public List<ApprovalHistory_t> getdetails() {
		List<ApprovalHistory_t> get = Service.getdetails();
		return get;
	}

	@GetMapping("/ApprovalHistory/{id}")
	public ApprovalHistory_t getdetailsbyId(@PathVariable Long id) {
		ApprovalHistory_t get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/ApprovalHistory/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/ApprovalHistory/{id}")
	public ApprovalHistory_t update(@RequestBody ApprovalHistory_t data, @PathVariable Long id) {
		ApprovalHistory_t update = Service.update(data, id);
		return update;
	}
	
//	@PutMapping("/updateServiceOrderStatus")
//    public ResponseEntity<ServiceOrderDto> updateServiceOrderStatus(
//            @RequestParam Long serviceOrderId,
//            @RequestParam String newStatus,
//            @RequestParam String actionedBy,
//            @RequestParam String comments
//    ) {
//        ServiceOrderDto updatedServiceOrder = Service.updateServiceOrderStatus(serviceOrderId, newStatus, actionedBy, comments);
//        return new ResponseEntity<>(updatedServiceOrder, HttpStatus.OK);
//    }
//    
}