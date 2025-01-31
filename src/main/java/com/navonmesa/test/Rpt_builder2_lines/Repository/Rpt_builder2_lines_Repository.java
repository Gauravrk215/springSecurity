package com.realnet.Rpt_builder2_lines.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
 

import com.realnet.Rpt_builder2_lines.Entity.Rpt_builder2_lines_t;

@Repository
public interface  Rpt_builder2_lines_Repository  extends  JpaRepository<Rpt_builder2_lines_t, Long>  { 
	@Query(value = "SELECT * FROM rpt_builder2_lines_t where rpt_builder2_t_id =?1", nativeQuery = true)
	Rpt_builder2_lines_t getRpt_builder2_lines(Long id);
}