package com.realnet.sysparameter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.sysparameter.entity.SysParamUpload;

@Repository
public interface SysparamUploadRepo extends JpaRepository<SysParamUpload, Integer> {
	@Query(value= "SELECT * FROM sys_param_upload WHERE sys_param_entity_id=?1", nativeQuery=true)
	List<SysParamUpload> findAllById(int sys_param_entity_id);

}
