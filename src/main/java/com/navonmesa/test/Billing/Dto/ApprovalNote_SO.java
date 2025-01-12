package com.realnet.Billing.Dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class ApprovalNote_SO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentSeq;
    private String actionTaken;
    private String actionNote;

    private Date actionDate;

    @ManyToOne
    @JoinColumn(name = "approval_queue_id")
    @JsonBackReference
    private ApprovalQueue_SO approvalQueueSO;
}
