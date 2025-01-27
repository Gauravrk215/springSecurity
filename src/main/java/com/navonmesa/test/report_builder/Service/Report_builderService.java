package com.realnet.report_builder.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.report_builder.Entity.Rp_Line;
import com.realnet.report_builder.Entity.Rp_builder;
import com.realnet.report_builder.Repository.ReportRepository;
import com.realnet.report_builder.Repository.Rp_lineRepository;

@Service
public class Report_builderService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private Rp_lineRepository rp_lineRepository;

	public Rp_builder Savedata(Rp_builder dashbord_Header) {
		return reportRepository.save(dashbord_Header);
	}

	public List<Rp_builder> getdetails() {
		// TODO Auto-generated method stub
		return (List<Rp_builder>) reportRepository.findAll();
	}

	public Rp_builder getdetailsbyId(int id) {
		// TODO Auto-generated method stub
		return reportRepository.findById(id);
	}

	public void delete_by_id(int id) {
		// TODO Auto-generated method stub
		reportRepository.deleteById(id);
	}

	public Rp_builder update_dashboard_header(int id, Rp_builder rp) {
		Rp_builder bi = reportRepository.findById(id);

		bi.setAccountId(rp.getAccountId());
		bi.setDescription(rp.getDescription());
		bi.setMenuName(rp.getMenuName());
		bi.setModule_id(rp.getModule_id());
		bi.setPage_size(rp.getPage_size());
		bi.setReport_builder_name(rp.getReport_builder_name());

		return reportRepository.save(bi);
	}

	public Rp_Line update_Dashbord1_Line(Rp_Line dashbord1_Line) {
		// TODO Auto-generated method stub
		return rp_lineRepository.save(dashbord1_Line);
	}

	public List<Rp_builder> get_by_module_id(int module_id) {
		// TODO Auto-generated method stub
		return (List<Rp_builder>) reportRepository.findbyModule(module_id);
	}

	public List<Rp_Line> get_all_lines() {
		// TODO Auto-generated method stub
		return (List<Rp_Line>) rp_lineRepository.findAll();
	}

	public Rp_Line update_Dashbord1_Lineby_id(int id, Rp_Line dashbord1_Line) {

		Rp_Line oldline = rp_lineRepository.findById(id);
//				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));

		oldline.setAccountId(dashbord1_Line.getAccountId());
		oldline.setHeader_id(dashbord1_Line.getHeader_id());
		oldline.setModel(dashbord1_Line.getModel());
		final Rp_Line newline = rp_lineRepository.save(oldline);
		return newline;
	}

}
