package com.realnet.BulkUpload.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.BulkUpload.Entity.MappingRule;

@Repository
public interface MappingRuleRepository extends JpaRepository<MappingRule, Long>{

}
