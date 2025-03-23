package com.realnet.api_registery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.api_registery.Entity.Token_registery;

@Repository
public interface Token_registery_Repository extends JpaRepository<Token_registery, Integer> {

	@Query(value = "select * from token_registery where created_by=?1", nativeQuery = true)

	List<Token_registery> getallbycreatedby(Long createdby);
}