package com.realnet.Billing.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.realnet.Billing.Entitys.ApprovalHistory_t;

@Repository
public interface ApprovalHistory_Repository extends JpaRepository<ApprovalHistory_t, Long> {
}