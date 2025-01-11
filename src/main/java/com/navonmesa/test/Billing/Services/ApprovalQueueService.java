package com.realnet.Billing.Services;

import com.realnet.Billing.Dto.ApprovalQueue_SO;
import com.realnet.Billing.Repositorys.ApprovalQueue_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalQueueService {

    @Autowired
    private ApprovalQueue_Repository approvalQueueRepository;

    public ApprovalQueue_SO saveApprovalQueue(ApprovalQueue_SO approvalQueueSOSO) {
        return approvalQueueRepository.save(approvalQueueSOSO);
    }

    public List<ApprovalQueue_SO> getAllApprovalQueue() {
        return approvalQueueRepository.findAll();
    }

    public ApprovalQueue_SO getApprovalQueueById(Long id) {
        return approvalQueueRepository.findById(id).orElse(null);
    }

    public List<ApprovalQueue_SO> getAllQueueForDocument(Long docid) {
        return approvalQueueRepository.findByDocumentSeq(docid);
    }
}
