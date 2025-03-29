package com.realnet.dashboard_builder_authsec.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
 

import com.realnet.dashboard_builder_authsec.Entity.Dashboard;

@Repository
public interface  DashboardRepository  extends  JpaRepository<Dashboard, Integer>  { 
}