package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShippingRepo extends JpaRepository<Shipping, Integer> {


        @Query(value = "select top 1 shipping_id,time_estimate,status from shippings where order_id = :orderId",nativeQuery = true)
        List<Object[]> findShippingByOrderId(Integer orderId);

        List<Shipping> findByShipper_UserId(Integer userId);
}
