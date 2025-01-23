package com.realnet.rb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.rb.entity.Rn_Rb_Where_Param;

@Repository
public interface Rn_rb_where_param_repository extends JpaRepository<Rn_Rb_Where_Param, Integer>
{

	@Query(value = "SELECT * from rn_rb_where_param_t WHERE REPORT_ID=:id", nativeQuery = true)
	List<Rn_Rb_Where_Param> getWhereByReport(@Param("id") int id);
	
}
