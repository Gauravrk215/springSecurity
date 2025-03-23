package com.realnet.api_registery.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
 

import com.realnet.api_registery.Entity.Api_registery_header;

@Repository
public interface  Api_registery_headerRepository  extends  JpaRepository<Api_registery_header, Long>  { 
}