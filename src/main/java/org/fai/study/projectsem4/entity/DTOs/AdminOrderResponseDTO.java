package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fai.study.projectsem4.entity.ENUM.EOrder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderResponseDTO {
    private Integer orderId;
    private String customerName;
    private String avatar;
    private String address;
    private String orderDate;
    private Double orderPrice;
    private String status;
}
