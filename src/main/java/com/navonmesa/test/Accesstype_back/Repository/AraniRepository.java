package com.realnet.Accesstype_back.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.Accesstype_back.Entity.Arani;

@Repository
public interface AraniRepository extends JpaRepository<Arani, Long> {

}
