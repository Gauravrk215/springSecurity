package com.realnet.Builders.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.Builders.Entity.Builder_entity_t;

@Repository
public interface BuilderRepository extends JpaRepository<Builder_entity_t, Long> {

	@Query(value = "select * from builder_entity_t where  job_name= ?1 && job_type=?2", nativeQuery = true)
	Builder_entity_t findByjobTypeAndName(String job_name, String job_type);

}
