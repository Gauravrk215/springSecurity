package com.realnet.Notification.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.Notification.Entity.NotEntity;
import com.realnet.Notification.Repository.NotRepo;

@RestController
@RequestMapping("/notification")
public class NotController {
	
	@Autowired
	private NotRepo notRepo;
	
//	@PostMapping("/save_notification")
//	  public NotEntity Savedata(@RequestBody NotEntity entity) {
//		NotEntity dash = notRepo.save(entity)	;
//		 return dash;
//	  }
		 
	
	@GetMapping("/get_notification")
	public List<NotEntity> getdetails() {
		 List<NotEntity> dash = notRepo.findTopByOrderByd();		
		return dash;
	}
	

}
