package com.realnet.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.template.entity.ImageUpload;

@Repository
public interface ImageUploadRepo extends JpaRepository<ImageUpload, Long> {

}
