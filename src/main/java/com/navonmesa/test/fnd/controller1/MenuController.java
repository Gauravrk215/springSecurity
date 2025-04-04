package com.realnet.fnd.controller1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.entity1.GrpMenuAccess;
import com.realnet.fnd.entity1.MenuDet;
import com.realnet.fnd.entity1.MixMenuNew;
import com.realnet.fnd.repository1.GrpMenuAccessRepository;
import com.realnet.fnd.repository1.MenuDetRepository;
import com.realnet.users.entity1.AppUserRole;
import com.realnet.users.repository1.AppUserRoleRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/api1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Rn_Menu_Group" })
@CrossOrigin("*")

public class MenuController {

	@Autowired
	private AppUserRoleRepository appUserRoleRepository;

	private MenuDetRepository menuDetRepository;
	private GrpMenuAccessRepository grpMenuAccessRepository;

	@Autowired
	public MenuController(MenuDetRepository menuDetRepository, GrpMenuAccessRepository grpMenuAccessRepository) {
		super();

		this.menuDetRepository = menuDetRepository;
		this.grpMenuAccessRepository = grpMenuAccessRepository;
	}

	@GetMapping("/allmenus")
	public ResponseEntity<?> getallmenu() {
		List<MixMenuNew> menu = menuDetRepository.getallmenu();
		return new ResponseEntity<>(menu, HttpStatus.OK);
	}

//		ADD DATA FOR SEC MENU DETAIL
	@PostMapping("/Sec_menuDet")
	public ResponseEntity<?> adddata(@RequestBody MenuDet menuDet) {
		MenuDet save = menuDetRepository.save(menuDet);
		return new ResponseEntity<>(save, HttpStatus.CREATED);
	}

//	GET BY MENU ITEM ID
	@GetMapping("/Sec_menuDet/{menu_item_id}")
	public ResponseEntity<?> getdata(@PathVariable Long menu_item_id) {
		MenuDet save = menuDetRepository.findById(menu_item_id)
				.orElseThrow(() -> new ResourceNotFoundException("id not found"));
		return new ResponseEntity<>(save, HttpStatus.CREATED);
	}

//	GET MENU AND SUBMENU of MENU DET
	@GetMapping("/submenu1")
	public List<MenuDet> submenu() {
		List<MenuDet> root = menuDetRepository.findAllRootsByMenuId();

		for (MenuDet m : root) {

			List<MenuDet> allSubmenu = menuDetRepository.findAllSubmenuByMenuId(m.getMenuItemId());
			MenuDet menu = menuDetRepository.findById(m.getMenuItemId()).orElse(null);
			menu.setSubMenus(allSubmenu);
		}

		return root;
	}

//	 Get Only SubMenu OF MENU DET
	@GetMapping("/submenu1/{menu_item_id}")
	public ResponseEntity<?> getonlysubmenu(@PathVariable Long menu_item_id) {
		List<MenuDet> submenu = menuDetRepository.findAllSubmenuByMenuId(menu_item_id);

		return new ResponseEntity<>(submenu, HttpStatus.OK);

	}

//	 GET MENU AND SUBMENU of GROUP MENU ACCESS
	@GetMapping("/grpmenuandsubmenu")
	public List<GrpMenuAccess> GRPMENUACCESS() {
		List<GrpMenuAccess> root = grpMenuAccessRepository.findAllRoots();

		for (int i = 0; i < root.size(); i++) {

			// find menu item id by menu id =0
			List<GrpMenuAccess> menu = grpMenuAccessRepository.findById(root.get(i).getUsrGrp().getUsrGrp(),
					root.get(i).getMenuItemId().getMenuItemId()); // menuItem id will be unique
			for (GrpMenuAccess g : menu) {

				List<GrpMenuAccess> allSubmenu = grpMenuAccessRepository
						.findAllSubmenuByMenuId(g.getMenuItemId().getMenuItemId(), g.getUsrGrp().getUsrGrp()); // find
																												// all
																												// submenu
																												// by
																												// menuid
																												// =
																												// menu
																												// itemid
				g.setSubMenus(allSubmenu);
			}

		}

		return root;
	}

//	 Get Only SubMenu of GROUP MENU ACCESS
	@GetMapping("/grpmenuandsubmenu/{menu_item_id}")
	public ResponseEntity<?> grpmenuaccess(@PathVariable Long menu_item_id) {
		List<GrpMenuAccess> submenu = grpMenuAccessRepository.findAllSubmenu(menu_item_id);

		return new ResponseEntity<>(submenu, HttpStatus.OK);

	}

//	 update by menu_item_id
	@PutMapping("/submenu1/{menu_item_id}")
	public ResponseEntity<?> updatedata(@RequestBody MenuDet d, @PathVariable Long menu_item_id) {
		MenuDet menu = menuDetRepository.findById(menu_item_id).orElseThrow(null);

		menu.setItemSeq(d.getItemSeq());
		menu.setMain_menu_action_name(d.getMain_menu_action_name());
		menu.setMain_menu_icon_name(d.getMain_menu_icon_name());
		menu.setMenuId(d.getMenuId());
		menu.setMenuItemDesc(d.getMenuItemDesc());
		menu.setModuleName(d.getModuleName());
		menu.setStatus(d.getStatus());

		MenuDet save = menuDetRepository.save(menu);
		return new ResponseEntity<>(save, HttpStatus.CREATED);
	}

//	 delete DATA SEC MENU DET WITH GRPMENUACCESS by menuItemId
	@DeleteMapping("/menu/{menu_item_id}")
	public ResponseEntity<?> delete(@PathVariable Long menu_item_id) {

		List<GrpMenuAccess> findlist = grpMenuAccessRepository.findlist(menu_item_id);
		System.out.println(" get grpmenu access data .." + findlist.size());

		if (!findlist.isEmpty()) {

			for (GrpMenuAccess g : findlist) {
				grpMenuAccessRepository.delete(g);
			}
		}
//		else {
//			GrpMenuAccess menuAccess = grpMenuAccessRepository.findbymenuitemid(menu_item_id);
//			grpMenuAccessRepository.delete(menuAccess);
//			System.out.println(" get grpmenu access data individualy data deleted ..");
//
//		}

		MenuDet menu = menuDetRepository.findById(menu_item_id).get();
		menuDetRepository.delete(menu);

		Optional<MenuDet> cmenu = menuDetRepository.findById(menu_item_id);

		if (!cmenu.isPresent()) {

			System.out.println(" Main Menu deleted ..");
			return new ResponseEntity<>("menu deleted", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("menu not deleted", HttpStatus.BAD_REQUEST);

		}

	}

//	 GET GROUP MENU ACCESS BY USR_GROUP with 	WITH SUBMENU

	@GetMapping("/grpmenuaccess/{usr_grp}")
	public ResponseEntity<?> GETGROUPMENU(@PathVariable Long usr_grp) {

		List<Object> list = new ArrayList<>();
		GrpMenuAccess grp = grpMenuAccessRepository.findByUsrGrp(usr_grp);

//		  List<MenuDet> sub = menuDetRepository.findAllSubmenuByMenuId(grp.getMenuItemId().getMenuItemId());
		List<MenuDet> sub = menuDetRepository.findAllSubmenuforusrgrp(grp.getMenuItemId().getMenuItemId());

		list.add(grp);
		list.add(sub);
		return new ResponseEntity<>(list, HttpStatus.OK);

	}

//	get all
	@GetMapping("/getAllData")
	public ResponseEntity<List<GrpMenuAccess>> getAllGroupMenuAccess() {
		List<GrpMenuAccess> listgrp = grpMenuAccessRepository.findAll();
		return new ResponseEntity<>(listgrp, HttpStatus.OK);
	}

//	get by  USRGRP WITHOUT SUBMENU

//			@GetMapping("/getById/{userId}")
//			public ResponseEntity<?> getGrpMenuByUserId(@PathVariable Long userId){
//				GrpMenuAccess grpMenuAccess=grpMenuAccessRepository.findByUsrGrp(userId);
//				if(grpMenuAccess==null)throw new ResourceNotFoundException("no resource found");
//				return new ResponseEntity<>(grpMenuAccess,HttpStatus.OK);
//			}

//			update by usr grp id
	@PutMapping("/updatemenuaccess/{userId}")
	public ResponseEntity<?> updateMenuAccess(@PathVariable Long userId, @RequestBody GrpMenuAccess grpMenuAccess) {
		GrpMenuAccess g1 = grpMenuAccessRepository.findByUsrGrp(userId);
		if (g1 == null) {
			throw new ResourceNotFoundException("no resource found");
		}
		g1.setMCreate(grpMenuAccess.getMCreate());
		g1.setMDelete(grpMenuAccess.getMDelete());
		g1.setMEdit(grpMenuAccess.getMEdit());
		g1.setMQuery(grpMenuAccess.getMQuery());
		g1.setMVisible(grpMenuAccess.getMVisible());
		g1.setMexport(grpMenuAccess.getMexport());

		GrpMenuAccess grpMenuAccess2 = grpMenuAccessRepository.save(g1);
		return new ResponseEntity<>(grpMenuAccess2, HttpStatus.OK);
	}

//			delete by usrgrp
	@DeleteMapping("/deleteMenuAcces/{userId}")
	public void deleteMenuAccess(@PathVariable Long userId) {
		GrpMenuAccess grpMenuAccess = grpMenuAccessRepository.findByUsrGrp(userId);
		if (Objects.isNull(grpMenuAccess))
			throw new ResourceNotFoundException("no resource found");
		grpMenuAccessRepository.delete(grpMenuAccess);
	}

//	 ADD MULTIPLE DATA FOR GROUP MENU ACCESS
	@PostMapping("/group")
	public ResponseEntity<?> addgroupmenuaccess(@RequestBody List<GrpMenuAccess> grp) {

		for (GrpMenuAccess g : grp) {
			MenuDet menu1 = menuDetRepository.findById(g.getGmenuid()).orElseThrow(null);
			AppUserRole a = appUserRoleRepository.findById(g.getGrpid()).orElseThrow(null);

			g.setUsrGrp(a);
			g.setMenuItemId(menu1);
		}
		List<GrpMenuAccess> save = grpMenuAccessRepository.saveAll(grp);
		return new ResponseEntity<>(save, HttpStatus.CREATED);
	}

//		 ADD DATA FOR group menu access
	@PostMapping("/singlegroup")
	public ResponseEntity<?> addsinglegroup(@RequestBody GrpMenuAccess grp) {

		MenuDet menu1 = menuDetRepository.findById(grp.getGmenuid()).orElseThrow(null);
		AppUserRole a = appUserRoleRepository.findById(grp.getGrpid()).orElseThrow(null);

		grp.setUsrGrp(a);
		grp.setMenuItemId(menu1);

		GrpMenuAccess save = grpMenuAccessRepository.save(grp);
		return new ResponseEntity<>(save, HttpStatus.CREATED);
	}

//			NEWWWW post MENU AND SUBMENU IN GROUPMENUACCESS

	@PostMapping("/addgrpwithsubmenu")
	public ResponseEntity<?> adds(@RequestBody GrpMenuAccess g) {

		List<Object> list = new ArrayList<>();

		Optional<GrpMenuAccess> a = grpMenuAccessRepository.findbygrpandmenuid(g.getGmenuid(), g.getGrpid());
		if (!a.isPresent()) {
			MenuDet me = menuDetRepository.findById(g.getGmenuid()).orElseThrow(null);
			g.setMenuItemId(me);
			g.setItemSeq(me.getItemSeq());
			g.setMenuItemDesc(me.getMenuItemDesc());
			g.setMenuItemDesc(me.getMenuItemDesc());
			g.setModuleName(me.getModuleName());
			g.setStatus(me.getStatus());
			g.setMain_menu_action_name(me.getMain_menu_action_name());
			g.setMain_menu_icon_name(me.getMain_menu_icon_name());
			g.setMenuId(me.getMenuId());

			AppUserRole app1 = appUserRoleRepository.findById(g.getGrpid()).orElseThrow(null);
			g.setUsrGrp(app1);
			g.setMCreate("true");
			g.setMDelete("true");
			g.setMEdit("true");
			g.setMQuery("true");
			g.setMVisible("true");
			g.setIsdisable("true");
			g.setMexport("true");

			g.setCreatedAt(new Date());
			g.setUpdatedAt(new Date());

			GrpMenuAccess save1 = grpMenuAccessRepository.save(g);
			list.add(save1);

			List<MenuDet> submenu = menuDetRepository.findAllSubmenuByMenuId(g.getGmenuid());

			for (int i = 0; i < submenu.size(); i++) {
				MenuDet m = menuDetRepository.findById(submenu.get(i).getMenuItemId()).orElseThrow(null);
				g.setMenuItemId(m);
				g.setItemSeq(m.getItemSeq());
				g.setMenuItemDesc(m.getMenuItemDesc());
				g.setModuleName(m.getModuleName());
				g.setStatus(m.getStatus());
				g.setMain_menu_action_name(m.getMain_menu_action_name());
				g.setMain_menu_icon_name(m.getMain_menu_icon_name());
				g.setMenuId(m.getMenuId());

				AppUserRole app = appUserRoleRepository.findById(g.getGrpid()).orElseThrow(null);
				g.setUsrGrp(app);
				g.setMCreate("true");
				g.setMDelete("true");
				g.setMEdit("true");
				g.setMQuery("true");
				g.setMVisible("true");
				g.setIsdisable("true");
				g.setMexport("true");

				g.setCreatedAt(new Date());
				g.setUpdatedAt(new Date());

				GrpMenuAccess save = grpMenuAccessRepository.save(g);
				list.add(save);
			}

			return new ResponseEntity<>(list, HttpStatus.CREATED);

		} else {
			return new ResponseEntity<>("already added menu", HttpStatus.BAD_REQUEST);
		}

	}

//		GET ALL GRPACCESS BY USRGRP
	@GetMapping("getusracces1/{usr_grp}")
	public ResponseEntity<?> getallbyusrgrp(@PathVariable Long usr_grp) {
		List<GrpMenuAccess> list = grpMenuAccessRepository.findAllByUsrGrp(usr_grp);
		return new ResponseEntity<>(list, HttpStatus.OK);

	}

//	UPDATE GRP MENU ACCESS BY MENU ITEMID
	@PutMapping("/update/{menu_item_id}/{usr_grp}")
	public ResponseEntity<?> updateMenuGRPAccess(@PathVariable Long menu_item_id, @PathVariable Long usr_grp,
			@RequestBody GrpMenuAccess grpMenuAccess) {
		GrpMenuAccess g1 = grpMenuAccessRepository.findByUsrgrpAndMenuitemid(menu_item_id, usr_grp);
		if (g1 == null) {
			throw new ResourceNotFoundException("no resource found");
		}
		g1.setMCreate(grpMenuAccess.getMCreate());
		g1.setMDelete(grpMenuAccess.getMDelete());
		g1.setMEdit(grpMenuAccess.getMEdit());
		g1.setMQuery(grpMenuAccess.getMQuery());
		g1.setMVisible(grpMenuAccess.getMVisible());
		g1.setIsdisable(grpMenuAccess.getIsdisable());
		g1.setItemSeq(grpMenuAccess.getItemSeq());
		g1.setMain_menu_action_name(grpMenuAccess.getMain_menu_action_name());
		g1.setMain_menu_icon_name(grpMenuAccess.getMain_menu_action_name());
		g1.setMenuId(grpMenuAccess.getMenuId());
		g1.setMenuItemDesc(grpMenuAccess.getMenuItemDesc());
		g1.setModuleName(grpMenuAccess.getModuleName());
		g1.setStatus(grpMenuAccess.getStatus());
		g1.setMexport(grpMenuAccess.getMexport());

		GrpMenuAccess grpMenuAccess2 = grpMenuAccessRepository.save(g1);
		return new ResponseEntity<>(grpMenuAccess2, HttpStatus.OK);
	}

//	DELETE GRPMENU ACCESS BY MENUITEMID AND USRGRP
	@DeleteMapping("/deleteGrpMenuAcces/{menu_item_id}/{usr_grp}")
	public void deleteGrpMenuAccess(@PathVariable Long menu_item_id, @PathVariable Long usr_grp) {
		GrpMenuAccess grpMenuAccess = grpMenuAccessRepository.findByUsrgrpAndMenuitemid(menu_item_id, usr_grp);
		if (Objects.isNull(grpMenuAccess))
			throw new ResourceNotFoundException("no resource found");
		grpMenuAccessRepository.delete(grpMenuAccess);
	}

//	GET GRPMENU ACCESS BY MENUITEM ID BUT NOT WORKING WITH SUBMENU
	@GetMapping("/getsec/{menu_item_id}")
	public ResponseEntity<?> getgrpmenuaccess(@PathVariable Long menu_item_id) {
		GrpMenuAccess get = grpMenuAccessRepository.findById1(menu_item_id);

		return new ResponseEntity<>(get, HttpStatus.CREATED);
	}

}
