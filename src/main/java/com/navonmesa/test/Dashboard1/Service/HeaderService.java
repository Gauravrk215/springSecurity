package com.realnet.Dashboard1.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.Dashboard1.Entity.Dashbord1_Line;
import com.realnet.Dashboard1.Entity.Dashbord_Header;
import com.realnet.Dashboard1.Repository.Dashboard_lineRepository;
import com.realnet.Dashboard1.Repository.HeaderRepository;

@Service
public class HeaderService {
	
	@Autowired
	private HeaderRepository headerRepository;
	
	@Autowired
	private Dashboard_lineRepository dashboard_lineRepository;

	public Dashbord_Header Savedata(Dashbord_Header dashbord_Header) {
		return headerRepository.save(dashbord_Header);	
	}

	
	public List<Dashbord_Header> getdetails() {
		// TODO Auto-generated method stub
		return (List<Dashbord_Header>) headerRepository.findAll();
	}

	
	public Dashbord_Header getdetailsbyId(int id) {
		// TODO Auto-generated method stub
		return headerRepository.findById(id);
	}


	public void delete_by_id(int id) {
		// TODO Auto-generated method stub
		 headerRepository.deleteById(id);
	}


	public Dashbord_Header update_dashboard_header(Dashbord_Header dashbord_Header) {
		return headerRepository.save(dashbord_Header);
	}
		



	public Dashbord1_Line update_Dashbord1_Line(Dashbord1_Line dashbord1_Line) {
		// TODO Auto-generated method stub
		return dashboard_lineRepository.save(dashbord1_Line);
	}


	public List<Dashbord_Header> get_by_module_id(int module_id) {
		// TODO Auto-generated method stub
		return (List<Dashbord_Header>) headerRepository.findbydashboardmodule(module_id);
	}


	public List<Dashbord1_Line> get_all_lines() {
		// TODO Auto-generated method stub
		return (List<Dashbord1_Line>) dashboard_lineRepository.findAll();
	}
	
//	public List<Dashbord_Header> get_by_module_id(int module_id) {
//		// TODO Auto-generated method stub
//		return (List<Dashbord_Header>) headerRepository.findAllById(module_id);
//	}
	
//	public List<Dashbord_Header> get_by_module_id(String module_id) {
//		// TODO Auto-generated method stub
//		return (List<Dashbord_Header>) headerRepository.findBymodule_id(module_id);
//	}

	public Dashbord1_Line update_Dashbord1_Lineby_id(int id, Dashbord1_Line dashbord1_Line) {
		
		
		Dashbord1_Line oldline= dashboard_lineRepository.findById(id);
//				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));

		
		oldline.setAccountId(dashbord1_Line.getAccountId());
		oldline.setHeader_id(dashbord1_Line.getHeader_id());
		oldline.setModel(dashbord1_Line.getModel());
		final Dashbord1_Line newline= dashboard_lineRepository.save(oldline);
		return newline;
	}
	




	

}
