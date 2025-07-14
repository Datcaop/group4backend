package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {

}
