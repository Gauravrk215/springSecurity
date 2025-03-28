package com.realnet.Dashboard_builder.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.Dashboard_builder.Entity.Dashboard_builder_t;

@Repository
public interface Dashboard_builder_Repository extends JpaRepository<Dashboard_builder_t, Long> {

	
	
}