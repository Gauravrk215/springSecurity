package com.realnet.FileUpload.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.FileUpload.Entity.Uploadeddocs;

@Repository
public interface UploadFileRepository extends JpaRepository<Uploadeddocs, Integer> {

	@Query(value = "select * from uploadeddocs  where ref=?1 && ref_table_name=?2", nativeQuery = true)
	List<Uploadeddocs> findbyrefAnsTableName(String ref, String ref_table_name);

}