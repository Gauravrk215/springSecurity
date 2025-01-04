package com.realnet.BulkUpload.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.BulkUpload.Entity.BulkUpload_t;
import com.realnet.BulkUpload.Entity.MappingRule;
import com.realnet.BulkUpload.Repository.MappingRuleRepository;

@Service
public class MappingRuleService {

	@Autowired
	private MappingRuleRepository Repository;

	public MappingRule Savedata(MappingRule data) {
		return Repository.save(data);
	}

	public List<MappingRule> getdetails() {
		return (List<MappingRule>) Repository.findAll();
	}

	public MappingRule getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public MappingRule update(MappingRule data, Long id) {
		MappingRule old = Repository.findById(id).get();
		old.setEntity_name(data.getEntity_name());
		old.setDescription(data.getDescription());
		old.setMapping_rule(data.getMapping_rule());
		old.setActive(data.isActive());
		final MappingRule test = Repository.save(old);
		return test;
	}
}
