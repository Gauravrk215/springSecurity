package com.realnet.Gaurav_testing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.Gaurav_testing.Entity.Gaurav_testing_t;

@Repository
public interface Gaurav_testing_Repository extends JpaRepository<Gaurav_testing_t, Integer> {

	Gaurav_testing_t findTopByOrderByIdAsc();

}