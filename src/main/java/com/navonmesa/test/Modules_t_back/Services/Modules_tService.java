package com.realnet.Modules_t_back.Services;

import com.realnet.Modules_t_back.Repository.Modules_tRepository;
import com.realnet.Modules_t_back.Entity.Modules_t;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Modules_tService {
	@Autowired
	private Modules_tRepository Repository;

	public Modules_t Savedata(Modules_t data) {
		data.setIsdeleted(false);
		Modules_t save = Repository.save(data);

		return save;
	}

	public List<Modules_t> getdetails() {
		return (List<Modules_t>) Repository.findAll();
	}

	public Modules_t getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public Modules_t update(Modules_t data, Long id) {
		Modules_t old = Repository.findById(id).get();
		old.setModules(data.getModules());
		old.setDescription(data.getDescription());
		old.setAccess_exclusive(data.getAccess_exclusive());
		final Modules_t test = Repository.save(old);
		return test;
	}
}