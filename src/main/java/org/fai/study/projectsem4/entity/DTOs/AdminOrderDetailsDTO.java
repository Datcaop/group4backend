package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderDetailsDTO {
    private Integer orderDetailId;
    private String image;
    private String productName;
    private String sku;
    private Double orderDetailPrice;
    private Integer quantity;
}
