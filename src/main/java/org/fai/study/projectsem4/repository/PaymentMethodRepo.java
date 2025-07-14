package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, Integer> {
}
