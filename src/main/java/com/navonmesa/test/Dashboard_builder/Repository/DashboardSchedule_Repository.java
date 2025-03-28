package com.realnet.Dashboard_builder.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.Dashboard_builder.Entity.DashboardSchedule_t;
 



@Repository
public interface  DashboardSchedule_Repository  extends  JpaRepository<DashboardSchedule_t, Long>  { 
	
	@Query(value = "SELECT * FROM dashboard_schedule_t where gatewaydone ='N'  order by id  asc", nativeQuery = true)
	List<DashboardSchedule_t> findTopByOrderByIdAsc();

	
}