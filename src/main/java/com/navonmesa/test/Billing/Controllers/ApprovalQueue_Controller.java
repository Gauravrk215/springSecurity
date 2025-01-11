package com.realnet.Billing.Controllers;

import com.realnet.Billing.Dto.ApprovalNote_SO;
import com.realnet.Billing.Dto.ApprovalQueue_SO;
import com.realnet.Billing.Repositorys.ApprovalReturnHistory_Repo;
import com.realnet.Billing.Services.ApprovalQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/billing/approval")
public class ApprovalQueue_Controller {

    @Autowired
    private ApprovalQueueService approvalQueueService;

    @PostMapping("/add")
    public ResponseEntity<?> addApprovalQueue(
            @RequestBody ApprovalQueue_SO approvalQueueSOSO
    ) {
        return ResponseEntity.ok(approvalQueueService.saveApprovalQueue(approvalQueueSOSO));
    }

    @GetMapping("/getAll")
    public List<ApprovalQueue_SO> getAllApprovalQueue() {
        return approvalQueueService.getAllApprovalQueue();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApprovalQueue_SO> getApprovalQueueById(
            @PathVariable Long id) {
        ApprovalQueue_SO approvalQueueSOSO = approvalQueueService.getApprovalQueueById(id);
        if (approvalQueueSOSO != null) {
            return ResponseEntity.ok(approvalQueueSOSO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getall/{docSeq}")
    public ResponseEntity<List<ApprovalQueue_SO>> getAllQueueForDoc(@PathVariable Long docSeq) {
        List<ApprovalQueue_SO> approvalQueueSOSOList = approvalQueueService.getAllQueueForDocument(docSeq);

        if (approvalQueueSOSOList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(approvalQueueSOSOList);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ApprovalQueue_SO> updateApprovalQueue(
            @PathVariable Long id, @RequestBody ApprovalQueue_SO updatedApprovalQueueSOSO) {
        ApprovalQueue_SO existingApprovalQueueSOSO = approvalQueueService.getApprovalQueueById(id);
        if (existingApprovalQueueSOSO != null) {
            existingApprovalQueueSOSO.setApprover(updatedApprovalQueueSOSO.getApprover());
            existingApprovalQueueSOSO.setActionType(updatedApprovalQueueSOSO.getActionType());
            existingApprovalQueueSOSO.setActionTaken(updatedApprovalQueueSOSO.getActionTaken());
            existingApprovalQueueSOSO.setComments(updatedApprovalQueueSOSO.getComments());
            existingApprovalQueueSOSO.setActionedAt(updatedApprovalQueueSOSO.getActionedAt());

            ApprovalQueue_SO updatedQueue = approvalQueueService.saveApprovalQueue(existingApprovalQueueSOSO);
            return ResponseEntity.ok(updatedQueue);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

         // *******************************************************************************  \\
        //                             History-Controller                                     \\

    @Autowired
    private ApprovalReturnHistory_Repo returnHistoryRepo;

    @PostMapping("/return")
    public ResponseEntity<?> saveReturnResponse(
            @RequestBody ApprovalNote_SO history
    ){
        history.setActionDate(new Date());
        System.out.println(history);
        return ResponseEntity.ok(returnHistoryRepo.save(history));
    }

}
