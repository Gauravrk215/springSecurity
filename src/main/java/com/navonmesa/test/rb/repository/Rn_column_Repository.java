package com.realnet.rb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.rb.entity.Rn_rb_Column;
import com.realnet.rb.response.Rn_columnDTO;

@Repository
public interface Rn_column_Repository extends JpaRepository<Rn_rb_Column, Integer>
{
	@Query(value = "SELECT * from rn_rb_column_t WHERE REPORT_ID=:id", nativeQuery = true)
	List<Rn_columnDTO> getList(@Param("id") int id);
	
	@Query(value = "SELECT * from rn_rb_column_t WHERE REPORT_ID=:id", nativeQuery = true)
	List<Rn_rb_Column> getColumnByReport(@Param("id") int id);
	
}
