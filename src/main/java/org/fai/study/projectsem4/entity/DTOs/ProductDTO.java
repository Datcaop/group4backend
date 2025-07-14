package org.fai.study.projectsem4.entity.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Integer productId;
    private String productName;
    private Double price;
    private Integer soldNumber;
    private String genderName;
    private String cateName;
    private String colorName;
    private String imageMain;
    private String imageSecond;
    private String imageThird;

}
