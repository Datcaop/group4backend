package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderDetailResponseDTO {
    private Integer orderId;
    private String orderDate;
    private List<AdminOrderDetailsDTO> orderDetails;
    private Double totalPrice;
    private String paymentMethod;
    private String paymentStatus;
    private Double paidMoney;
    private AdminGetUserInforDTO customerInformation;
    private ShippingDTO shippingInformation;

}
