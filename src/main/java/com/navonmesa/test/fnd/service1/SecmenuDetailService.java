package com.realnet.fnd.service1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.fnd.entity1.GrpMenuAccess;
import com.realnet.fnd.entity1.MenuDet;
import com.realnet.fnd.repository1.GrpMenuAccessRepository;
import com.realnet.fnd.repository1.MenuDetRepository;
import com.realnet.users.entity1.AppUserRole;
import com.realnet.users.repository1.AppUserRoleRepository;

@Service
public class SecmenuDetailService {

	@Autowired
	private MenuDetRepository menuDetRepository;

	@Autowired
	private AppUserRoleRepository appUserRoleRepository;

	@Autowired
	private GrpMenuAccessRepository grpMenuAccessRepository;

	public MenuDet customsecmenuadd(String tablename, Long menuId) {

		MenuDet menuDet = new MenuDet();
		String origTableName = tablename.replaceAll("_", " ");

		menuDet.setMenuId(menuId);
		menuDet.setItemSeq(8001l);
		menuDet.setMain_menu_icon_name(tablename);
		menuDet.setMenuItemDesc(origTableName);
		menuDet.setModuleName(tablename);
		menuDet.setMain_menu_action_name(tablename);

		menuDet.setStatus("Enable");

		MenuDet save = menuDetRepository.save(menuDet);

		sync(menuId, new GrpMenuAccess());

		System.out.println(save.getMenuItemDesc() + " menu inserted");

		return save;

	}

//	sync service
	public List<Object> sync(Long gmenuid, GrpMenuAccess gr) {

		List<Object> list = new ArrayList<>();
		MenuDet formenu = menuDetRepository.findById(gmenuid).orElseThrow(null);

		List<GrpMenuAccess> all = grpMenuAccessRepository.findByGrpAndMenuid1(formenu.getMenuItemId());

		for (GrpMenuAccess a : all) {

			a.setMenuItemId(formenu);
			a.setItemSeq(formenu.getItemSeq());
			a.setMenuItemDesc(formenu.getMenuItemDesc());
			a.setModuleName(formenu.getModuleName());
			a.setStatus(formenu.getStatus());
			a.setMain_menu_action_name(formenu.getMain_menu_action_name());
			a.setMain_menu_icon_name(formenu.getMain_menu_icon_name());
			a.setMenuId(formenu.getMenuId());
			GrpMenuAccess save = grpMenuAccessRepository.save(a);
			list.add(save);
		}

		List<MenuDet> submenu = menuDetRepository.findAllSubmenuByMenuId(gmenuid);
		for (int i = 0; i < submenu.size(); i++) {
			MenuDet m = menuDetRepository.findById(submenu.get(i).getMenuItemId()).orElseThrow(null);
			List<GrpMenuAccess> li = grpMenuAccessRepository.findlist(m.getMenuItemId());

			if (li.isEmpty()) {
				List<GrpMenuAccess> grp = grpMenuAccessRepository.findAllById(gmenuid);

				for (GrpMenuAccess a : grp) {
					AppUserRole app = appUserRoleRepository.findById(a.getUsrGrp().getUsrGrp()).orElseThrow(null);
					gr.setUsrGrp(app);
					gr.setMenuItemId(m);

					gr.setItemSeq(m.getItemSeq());
					gr.setMenuItemDesc(m.getMenuItemDesc());
					gr.setModuleName(m.getModuleName());
					gr.setStatus(m.getStatus());
					gr.setMain_menu_action_name(m.getMain_menu_action_name());
					gr.setMain_menu_icon_name(m.getMain_menu_icon_name());
					gr.setMenuId(m.getMenuId());

					gr.setMCreate("true");
					gr.setMDelete("true");
					gr.setMEdit("true");
					gr.setMQuery("true");
					gr.setMVisible("true");
					gr.setIsdisable("true");
					gr.setMexport("true");

					gr.setCreatedAt(new Date());
					gr.setUpdatedAt(new Date());

					GrpMenuAccess save2 = grpMenuAccessRepository.save(gr);
					list.add(save2);
				}

			}

			else {
				for (GrpMenuAccess g : li) {

					g.setMenuItemId(m);

					g.setItemSeq(m.getItemSeq());
					g.setMenuItemDesc(m.getMenuItemDesc());
					g.setModuleName(m.getModuleName());
					g.setStatus(m.getStatus());
					g.setMain_menu_action_name(m.getMain_menu_action_name());
					g.setMain_menu_icon_name(m.getMain_menu_icon_name());
					g.setMenuId(m.getMenuId());

					GrpMenuAccess save1 = grpMenuAccessRepository.save(g);
					list.add(save1);
				}
			}
		}
		return list;

	}

}
