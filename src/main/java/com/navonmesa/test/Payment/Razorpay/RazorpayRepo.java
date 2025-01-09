package com.realnet.Payment.Razorpay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RazorpayRepo extends JpaRepository<RazorpayDTO,Long> {
    RazorpayDTO findByOrderId(String orderId);
}
