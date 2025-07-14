package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersShipperDTO {
    private Integer orderId;
    private Integer shippingId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private String orderDate;
    private Double totalPrice;
    private String status;
}
