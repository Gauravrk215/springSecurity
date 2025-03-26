package com.realnet.Dashboard1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.realnet.Dashboard1.Entity.Dashbord_Header;

@Repository
public interface HeaderRepository extends CrudRepository<Dashbord_Header, Integer> {

	Dashbord_Header findById(int id);

//	@Query(
//			value= " select * from dashboard_header where module_id=?1",nativeQuery=true)
//			List<Dashbord_Header> findbydashboardmodule(int moduleId);

//	public List<Dashbord_Header> findBymodule_id(String module_id);

	@Query(value = " select * from dashbord_header where module_id=?1", nativeQuery = true)
	List<Dashbord_Header> findbydashboardmodule(int module_id);

//	@Query(
//			"select u from Dashbord_Header u WHERE u.module_id =:n")
//			List<Dashbord_Header> getBymoduleId(int module_id);

	@Query(value = "select count(id) from dashbord_header", nativeQuery = true)
	public List<Object> findCount();

	@Query(value = "SELECT count(id) FROM realnet_CNSBE.dashbord_header where module_id=?1", nativeQuery = true)
	String count_dashboardheader(Integer moduleId);

}
