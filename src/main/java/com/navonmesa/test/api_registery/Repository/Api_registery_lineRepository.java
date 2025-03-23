package com.realnet.api_registery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.api_registery.Entity.Api_registery_line;

@Repository
public interface Api_registery_lineRepository extends JpaRepository<Api_registery_line, Integer> {

	@Query(value = "select * from api_registery_line where header_id=?1", nativeQuery = true)
	List<Api_registery_line> getLinesbyheaderid(Long header_id);

}