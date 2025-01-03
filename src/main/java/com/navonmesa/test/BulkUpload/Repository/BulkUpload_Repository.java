package com.realnet.BulkUpload.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
 

import com.realnet.BulkUpload.Entity.BulkUpload_t;


@Repository
public interface  BulkUpload_Repository  extends  JpaRepository<BulkUpload_t, Long>  {
	
	@Query(value = "SELECT * FROM bulk_upload_t where entity_name =?1", nativeQuery = true)
	BulkUpload_t getentityName(String name);
	
//	@Query(value = "SELECT * FROM bulk_upload_t where entity_name =?1", nativeQuery = true)
//	BulkUpload_t getByName(String name);
	
}