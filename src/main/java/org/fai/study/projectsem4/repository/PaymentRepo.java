package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Integer> {
    Payment findByOrder_OrderId(Integer orderId);
}
