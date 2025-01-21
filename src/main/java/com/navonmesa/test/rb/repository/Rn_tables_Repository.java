package com.realnet.rb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.rb.entity.Rn_rb_Column;
import com.realnet.rb.entity.Rn_rb_Tables;

@Repository
public interface Rn_tables_Repository extends JpaRepository<Rn_rb_Tables, Integer>
{

	@Query(value = "SELECT * from rn_rb_tables_t WHERE REPORT_ID=:id", nativeQuery = true)
	List<Rn_rb_Tables> getTablesByReport(@Param("id") int id);
	

}
