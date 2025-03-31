package com.realnet.FabricIcard.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.realnet.FabricIcard.Entity.FabricIcard;

@Repository
public interface  FabricIcardRepository  extends  JpaRepository<FabricIcard, Long>  { 
}