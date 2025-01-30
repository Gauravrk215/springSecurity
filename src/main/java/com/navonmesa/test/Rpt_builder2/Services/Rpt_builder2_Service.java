package com.realnet.Rpt_builder2.Services;

import com.realnet.Rpt_builder2.Repository.Rpt_builder2_Repository;
import com.realnet.Rpt_builder2.Entity.Rpt_builder2_t;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Rpt_builder2_Service {
	@Autowired
	private Rpt_builder2_Repository Repository;

	public Rpt_builder2_t Savedata(Rpt_builder2_t data) {
		return Repository.save(data);
	}

	public List<Rpt_builder2_t> getdetails() {
		return (List<Rpt_builder2_t>) Repository.findAll();
	}

	public Rpt_builder2_t getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public Rpt_builder2_t update(Rpt_builder2_t data, Long id) {
		Rpt_builder2_t old = Repository.findById(id).get();
//		old.setConn_name(data.getConn_name());
//		old.setDate_param_req(data.getDate_param_req());
//		old.setStd_param_html(data.getStd_param_html());
//		old.setAdhoc_param_html(data.getAdhoc_param_html());
//		old.setColumn_str(data.getColumn_str());
//		old.setSql_str(data.getSql_str());
		
		old.setReportName(data.getReportName());
		old.setDescription(data.getDescription());
		old.setActive(data.getActive());
//		old.setFolderName(data.getFolderName());
		
		
		final Rpt_builder2_t test = Repository.save(old);
		return test;
	}
}