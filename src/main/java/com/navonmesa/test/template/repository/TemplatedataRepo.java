package com.realnet.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.realnet.template.entity.TemplateFileUpload;

public interface TemplatedataRepo extends JpaRepository<TemplateFileUpload, Long> {

	@Query(value = "select * from template_file_upload where file_name =?1", nativeQuery = true)
	TemplateFileUpload getbyFileName(String fileName);

	@Query("SELECT t FROM TemplateFileUpload t WHERE t.entity_name IS NOT NULL AND t.isProcessed = false ORDER BY t.id ASC")
	List<TemplateFileUpload> findUnprocessedRecordsOrderedByIdAsc();


}
