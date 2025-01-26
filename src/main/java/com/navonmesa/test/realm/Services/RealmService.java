package com.realnet.realm.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.api_registery.Entity.Api_registery_header;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.realm.Entity.Realm;
import com.realnet.realm.Repository.RealmRepository;

@Service
public class RealmService {
	@Autowired
	private RealmRepository realmRepository;

	public Realm Savedata(Realm data) {

		Realm save = realmRepository.save(data);
		return save;
	}

//	get all with pagination
	public Page<Realm> getAllWithPagination(Pageable page) {
		return realmRepository.findAll(page);
	}

	public List<Realm> getdetails() {
		return (List<Realm>) realmRepository.findAll();
	}

	public Realm getdetailsbyId(Long id) {
		return realmRepository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		realmRepository.deleteById(id);
	}

	public Realm update(Realm data, Long id) {
		Optional<Realm> old1 = realmRepository.findById(id);

		if (old1.isPresent()) {

			Realm old = old1.get();

			old.setRealm_name(data.getRealm_name());

			final Realm test = realmRepository.save(old);
			return test;
		} else {
			throw new ResourceNotFoundException("not found");
		}

	}

	public List<Realm> findByUserId(Long userId) {

		List<Realm> list = realmRepository.findBytouserId(userId);
		return list;

	}
}
