package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingResponseDTO {
    private Integer shippingId;
    private String timeEstimate;
    private String shippingStatus;
    private Integer shipperId;
    private Integer orderId;
}
