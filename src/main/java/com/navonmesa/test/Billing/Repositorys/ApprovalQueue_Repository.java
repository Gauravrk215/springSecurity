package com.realnet.Billing.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.Billing.Dto.ApprovalQueue_SO;

import java.util.List;

@Repository
public interface ApprovalQueue_Repository extends JpaRepository<ApprovalQueue_SO, Long> {
    List<ApprovalQueue_SO> findByDocumentSeq(Long docid);
}
