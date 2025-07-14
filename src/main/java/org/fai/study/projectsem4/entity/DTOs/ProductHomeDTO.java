package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductHomeDTO {
    private Integer productId;
    private String productName;
    private Double price;
    private Integer soldNumber;
    private String image_url;
}
