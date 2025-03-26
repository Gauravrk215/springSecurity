package com.realnet.Dashboard1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.Dashboard1.Entity.Dashbord1_Line;

@Repository
public interface Dashboard_lineRepository extends CrudRepository<Dashbord1_Line, Integer> {

	Dashbord1_Line findById(int id);
	
	@Query(value = "SELECT * FROM realnet_CNSBE.dashbord1_line where dashbord_header_id =:id", nativeQuery = true)
	List<Dashbord1_Line> getByheader(@Param("id") int id);

}
