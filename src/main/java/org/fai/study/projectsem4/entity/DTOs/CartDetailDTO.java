package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDetailDTO {
    private Integer cartId;
    private Integer productId;
    private String url;
    private String name;
    private String code;
    private Integer size;
    private Integer quantity;
    private Double price;
}
