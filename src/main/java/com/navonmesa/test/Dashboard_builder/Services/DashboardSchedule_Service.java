package com.realnet.Dashboard_builder.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.Dashboard_builder.Entity.DashboardSchedule_t;
import com.realnet.Dashboard_builder.Repository.DashboardSchedule_Repository;

@Service
public class DashboardSchedule_Service {
	@Autowired
	private DashboardSchedule_Repository Repository;

	public DashboardSchedule_t Savedata(DashboardSchedule_t data) {
		data.setGatewaydone("N");
	//	DashboardSchedule_t save = Repository.save(job);
		return Repository.save(data);
	}

	public List<DashboardSchedule_t> getdetails() {
		return (List<DashboardSchedule_t>) Repository.findAll();
	}

	public DashboardSchedule_t getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public DashboardSchedule_t update(DashboardSchedule_t data, Long id) {
		DashboardSchedule_t old = Repository.findById(id).get();
		old.setCron(data.getCron());
		old.setEvery(data.getEvery());
		old.setGateway(data.getGateway());
		old.setTemplate(data.getTemplate());
		old.setStartTime(data.getStartTime());
		old.setEndTime(data.getEndTime());
		old.setAttachment(data.getAttachment());
		old.setSendTo(data.getSendTo());
		final DashboardSchedule_t test = Repository.save(old);
		return test;
	}
}