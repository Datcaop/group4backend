package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.DTOs.AdminOrderDetailResponseDTO;
import org.fai.study.projectsem4.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    @Query(value = "select o.order_id,u.full_name,o.address,o.order_date,o.total_price,o.status " +
            "from orders o join users u on o.user_id = u.user_id",nativeQuery = true)
    List<Object[]> getAllOrder();

    @Query(value = "select o.order_id,o.order_date,od.order_detail_id,(select i.url from images i where i.product_id = p.product_id order by i.image_id ASC LIMIT 1) as image,p.product_name,p.code,od.price_at_order,od.quantity,o.total_price,pm.method_name,p2.status,p2.paid_money,u.user_id,u.full_name,u.email,o.phone_number,o.address,s.shipping_id,s.status,s.image from orders o " +
            "inner join order_detail od on o.order_id = od.order_id " +
            "inner join users u on o.user_id = u.user_id " +
            "inner join  products p on od.product_id = p.product_id " +
            "inner join payment p2 on o.order_id = p2.order_id " +
            "inner join payment_method pm on p2.payment_method_id = pm.payment_method_id " +
            "left join shippings s on s.order_id = o.order_id "+
            "where o.order_id=:orderId",nativeQuery = true)
    List<Object[]> getOrderDetailByOrderIdAdmin(@Param("orderId") Integer orderId);

    @Query(value = "select o.order_id,u.full_name,o.address,o.order_date,o.total_price,o.status " +
            "from orders o join users u on o.user_id = u.user_id where o.user_id = :userId",nativeQuery = true)
    List<Object[]> getAllOrderUser(@Param("userId") Integer userId);

    @Query(value = "select o.order_id,s.shipping_id,u.full_name,o.phone_number,o.address,o.order_date,o.total_price,s.status " +
            "from orders o join users u on o.user_id = u.user_id " +
            "join shippings s on s.order_id = o.order_id " +
            "where s.shipper_id=:shipperId",nativeQuery = true)
    List<Object[]> getAllOrderByShipperId(@Param("shipperId") Integer shipperId);

    @Query(value = "SELECT DATE_FORMAT(order_date, '%m-%Y') AS month, COUNT(*) AS orderCount, SUM(total_price) AS totalRevenue " +
            "FROM orders " +
            "GROUP BY DATE_FORMAT(order_date, '%m-%Y')",nativeQuery = true)
    List<Object[]> getOrderMonthlyDashboard();

    @Query(value = "SELECT status, COUNT(*) AS totalOrders " +
            "FROM orders " +
            "GROUP BY status",nativeQuery = true)
    List<Object[]> getStatusOrderDashboard();
}
