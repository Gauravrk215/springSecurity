package com.realnet.Billing.Job.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.Billing.Job.Entity.BillingJobEntity;



@Repository
public interface BillingJobRepository extends  JpaRepository<BillingJobEntity, Long> {

	@Query(value = "SELECT * FROM billing_job_entity where job_type=?1", nativeQuery = true)
	BillingJobEntity getByJobType(String jobtype);
}
