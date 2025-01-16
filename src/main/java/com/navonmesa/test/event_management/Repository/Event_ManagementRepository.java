package com.realnet.event_management.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
 

import com.realnet.event_management.Entity.Event_Management;

@Repository
public interface  Event_ManagementRepository  extends  JpaRepository<Event_Management, Integer>  { 
}