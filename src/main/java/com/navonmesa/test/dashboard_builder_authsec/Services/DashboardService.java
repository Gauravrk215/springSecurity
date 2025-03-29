package com.realnet.dashboard_builder_authsec.Services;
import com.realnet.dashboard_builder_authsec.Repository.DashboardRepository;
import com.realnet.dashboard_builder_authsec.Entity.Dashboard;import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.realnet.SequenceGenerator.Service.SequenceService;
	import org.springframework.stereotype.Service;

@Service
 public class DashboardService {
@Autowired
private DashboardRepository Repository;






public Dashboard Savedata(Dashboard data) {






				return Repository.save(data);	
			}

			
public List<Dashboard> getdetails() {
				return (List<Dashboard>) Repository.findAll();
			}


public Dashboard getdetailsbyId(Integer id) {
	return Repository.findById(id).get();
			}


	public void delete_by_id(Integer id) {
 Repository.deleteById(id);
}


public Dashboard update(Dashboard data,Integer id) {
	Dashboard old = Repository.findById(id).get();
old.setName(data.getName());

old.setModel(data.getModel());

old.setIsdashboard (data.isIsdashboard());

final Dashboard test = Repository.save(old);
  return test;}}
