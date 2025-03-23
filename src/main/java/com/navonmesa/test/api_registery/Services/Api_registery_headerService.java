package com.realnet.api_registery.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.api_registery.Entity.Api_registery_header;
import com.realnet.api_registery.Repository.Api_registery_headerRepository;
import com.realnet.exceptions.ResourceNotFoundException;

@Service
public class Api_registery_headerService {
	@Autowired
	private Api_registery_headerRepository Repository;

	public Api_registery_header Savedata(Api_registery_header data) {

		Api_registery_header save = Repository.save(data);
		return save;
	}

//	get all with pagination
	public Page<Api_registery_header> getAllWithPagination(Pageable page) {
		return Repository.findAll(page);
	}

	public List<Api_registery_header> getdetails() {
		return (List<Api_registery_header>) Repository.findAll();
	}

	public Api_registery_header getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public Api_registery_header update(Api_registery_header data, Long id) {
		Optional<Api_registery_header> old1 = Repository.findById(id);

		if (old1.isPresent()) {

			Api_registery_header old = old1.get();

			old.setTable_name(data.getTable_name());

			final Api_registery_header test = Repository.save(old);
			return test;
		} else {
			throw new ResourceNotFoundException("not found");		
			}

	}
}
