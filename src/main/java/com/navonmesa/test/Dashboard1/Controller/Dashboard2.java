//package com.realnet.Dashboard1.Controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.realnet.Dashboard1.Entity.Dashbord_Header;
//import com.realnet.Dashboard1.Repository.HeaderRepository;
//import com.realnet.Dashboard1.Service.HeaderService;
//
//@RestController
//@RequestMapping("/token/dashboard2")
//public class Dashboard2 {
//	
//	@Autowired
//	private HeaderService headerService;
//
//	@Autowired
//	private HeaderRepository headerRepository;
//	
//	@GetMapping("/getdashboardbyname/{name}")
//	public Integer getdetailsbyId(@PathVariable String name) {
//		Dashbord_Header dash = headerRepository.findByDashboardName(name);
//		Integer id=dash.getId();
//		return id;
//	}
//
//}
