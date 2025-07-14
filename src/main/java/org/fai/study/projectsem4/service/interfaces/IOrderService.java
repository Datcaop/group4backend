package org.fai.study.projectsem4.service.interfaces;

import org.fai.study.projectsem4.entity.DTOs.*;

import java.util.List;

public interface IOrderService {
    String addNewOrder(OrderDTO orderDTO,Integer userId);
    String addNewVnPayOrder(OrderDTO orderDTO,Integer userId);
    List<AdminOrderResponseDTO> getAllOrderAdmin();
    AdminOrderDetailResponseDTO getOrderDetailInforAdminByOrderId(Integer orderId);
    List<MonthRevenueDashboardDTO> getMonthRevenueDashboard();
    List<StatusOrderDashboardDTO> getStatusOrderDashboard();
    List<UserRoleDTO> getUserRoleDashboard();
    String tranferOrderStatus(Integer orderId);
    List<AdminOrderResponseDTO> getAllOrderUser(Integer userId);
}
