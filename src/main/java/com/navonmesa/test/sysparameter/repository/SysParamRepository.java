package com.realnet.sysparameter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.sysparameter.entity.SysParamEntity;

@Repository
public interface SysParamRepository extends JpaRepository<SysParamEntity, Integer> {

}
