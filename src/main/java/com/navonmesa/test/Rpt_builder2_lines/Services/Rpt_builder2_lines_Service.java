package com.realnet.Rpt_builder2_lines.Services;
import com.realnet.Rpt_builder2_lines.Repository.Rpt_builder2_lines_Repository;
import com.realnet.Rpt_builder2_lines.Entity.Rpt_builder2_lines_t;import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

@Service
 public class Rpt_builder2_lines_Service {
@Autowired
private Rpt_builder2_lines_Repository Repository;
public Rpt_builder2_lines_t Savedata(Rpt_builder2_lines_t data) {
				return Repository.save(data);	
			}

			
public List<Rpt_builder2_lines_t> getdetails() {
				return (List<Rpt_builder2_lines_t>) Repository.findAll();
			}


public Rpt_builder2_lines_t getdetailsbyId(Long id) {
	return Repository.findById(id).get();
			}


	public void delete_by_id(Long id) {
 Repository.deleteById(id);
}


public Rpt_builder2_lines_t update(Rpt_builder2_lines_t data,Long id) {
	Rpt_builder2_lines_t old = Repository.findById(id).get();
old.setHeader_id(data.getHeader_id());
old.setModel(data.getModel());
final Rpt_builder2_lines_t test = Repository.save(old);
		return test;}}