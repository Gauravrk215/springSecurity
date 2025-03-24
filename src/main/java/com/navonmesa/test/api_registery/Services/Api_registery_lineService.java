package com.realnet.api_registery.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.api_registery.Entity.Api_registery_line;
import com.realnet.api_registery.Repository.Api_registery_lineRepository;
import com.realnet.exceptions.ResourceNotFoundException;

@Service
public class Api_registery_lineService {
	@Autowired
	private Api_registery_lineRepository Repository;

	public Api_registery_line Savedata(Api_registery_line data) {

		Api_registery_line save = Repository.save(data);
		return save;
	}

//	get all with pagination
	public Page<Api_registery_line> getAllWithPagination(Pageable page) {
		return Repository.findAll(page);
	}

	public List<Api_registery_line> getdetails() {
		return (List<Api_registery_line>) Repository.findAll();
	}

//	get lines by header id
	public List<Api_registery_line> getLinesbyHeaderid(Long header_id) {

		List<Api_registery_line> list = Repository.getLinesbyheaderid(header_id);
		return list;
	}

	public Api_registery_line getdetailsbyId(Integer id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Integer id) {
		Repository.deleteById(id);
	}

	public Api_registery_line update(Api_registery_line data, Integer id) {
		Optional<Api_registery_line> old1 = Repository.findById(id);

		if (old1.isPresent()) {
			Api_registery_line old = old1.get();
			old.setUrl(data.getUrl());

			old.setMethod(data.getMethod());

			old.setHeader_id(data.getHeader_id());

			final Api_registery_line test = Repository.save(old);
			return test;
		} else {
			throw new ResourceNotFoundException("not found");
		}
	}
}
