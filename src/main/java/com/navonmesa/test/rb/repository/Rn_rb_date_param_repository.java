package com.realnet.rb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.rb.entity.Rn_Rb_Date_String;

@Repository 
public interface Rn_rb_date_param_repository extends JpaRepository<Rn_Rb_Date_String, Integer>{

}
