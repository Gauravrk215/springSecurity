package com.realnet.Modules_t_back.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
 

import com.realnet.Modules_t_back.Entity.Modules_t;

@Repository
public interface  Modules_tRepository  extends  JpaRepository<Modules_t, Long>  { 
}