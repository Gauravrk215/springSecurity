package com.realnet.FromExtensionJson.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
 

import com.realnet.FromExtensionJson.Entity.FromExtensionJson_t;

@Repository
public interface  FromExtensionJson_Repository  extends  JpaRepository<FromExtensionJson_t, Long>  {

	
	@Query(value="SELECT * FROM from_extension_json_t  WHERE form_code = ?1", nativeQuery = true)
	FromExtensionJson_t getdetailsbyFormCode(String formCode); 
	
	@Query(value = "SELECT json_object FROM from_extension_json_t WHERE form_code = ?1", nativeQuery = true)
	List<String> getJsonObjectsByFormCode(String formCode);

}