package com.realnet.Accesstype_back.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.Accesstype_back.Entity.Agyana;

@Repository
public interface AgyanaRepository extends JpaRepository<Agyana, Long>{

}
