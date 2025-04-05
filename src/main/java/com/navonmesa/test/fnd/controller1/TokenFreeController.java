package com.realnet.fnd.controller1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.Dashboard1.Entity.Dashbord_Header;
import com.realnet.Dashboard1.Service.HeaderService;
import com.realnet.fnd.entity1.MenuDet;
import com.realnet.fnd.service1.SecmenuDetailService;

@RestController
@RequestMapping("/token")
public class TokenFreeController {

	@Autowired
	private SecmenuDetailService secmenuDetailService;

	@Autowired
	private HeaderService headerService;

//	add Custom sec menu detail
	@PostMapping("/Sec_menuDet/custom")
	public ResponseEntity<?> addCustomMenu(@RequestParam String tableName, @RequestParam Long menuid) {

		MenuDet menuDet = secmenuDetailService.customsecmenuadd(tableName, menuid);

		return new ResponseEntity<>(menuDet, HttpStatus.CREATED);
	}

//	add dashboard
	@PostMapping("/dashboard")
	public Dashbord_Header Savedata(@RequestBody Dashbord_Header dashbord_Header) {
		Dashbord_Header dash = headerService.Savedata(dashbord_Header);
		return dash;
	}

}
