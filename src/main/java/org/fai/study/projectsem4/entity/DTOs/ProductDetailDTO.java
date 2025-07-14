package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailDTO {
    private Integer productId;
    private String code;
    private String name;
    private Double price;
    private Integer sold;
    private String mainImage;
    private String image2;
    private String image3;
    private DescriptionProductDTO description;
}
