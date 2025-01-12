package com.realnet.Billing.Repositorys;

import com.realnet.Billing.Dto.ApprovalNote_SO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalReturnHistory_Repo extends JpaRepository<ApprovalNote_SO,Long> {
}
