package com.realnet.rb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.rb.entity.Rn_report_builder;

@Repository
public interface Rn_report_builder_repository extends JpaRepository<Rn_report_builder, Integer>{

	
	//@Query(value = ":query", nativeQuery = true)
   // List<Object[]> getQueryData(@Param("sql_query") String query);

	@Query(value = "SELECT * from rn_rb_reports_t WHERE id=:id", nativeQuery = true)
	List<Rn_report_builder> getByReportId(@Param("id") int id);
	
	@Query(value= "SELECT * from rn_rb_reports_t WHERE report_type='service' and id=:id",nativeQuery = true)
	List<Rn_report_builder> getreportbyservice(@Param("id") int id);

	@Query(value= "select schema_name from information_schema.schemata",nativeQuery = true)
	List<Object> getdatabaseList();

	@Query(value= "SELECT table_name FROM information_schema.tables WHERE table_schema =?1",nativeQuery = true)
	List<String> getListOftables(String table_schema);

	@Query(value= "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE `TABLE_SCHEMA`=:'TABLE_SCHEMA' AND `TABLE_NAME`=:'TABLE_NAME';",nativeQuery = true)
	List<Object> getallcolumnlist(@Param("TABLE_SCHEMA") Object table_schema, @Param("TABLE_NAME") Object tABLE_NAME);
	
	@Query(value = "SELECT count(id) FROM realnet_CNSBE.rn_rb_reports_t where module_id=?1", nativeQuery = true)
	String count_report(Integer moduleId);
}
