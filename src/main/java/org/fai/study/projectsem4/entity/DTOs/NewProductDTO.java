package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductDTO {
    private String brand;
    private String code;
    private Double price;
    private String productName;
    private Integer soldNumber;
    private Double weight;
    private Integer genderId;
    private Integer colorId;
    private Integer categoryId;

}
