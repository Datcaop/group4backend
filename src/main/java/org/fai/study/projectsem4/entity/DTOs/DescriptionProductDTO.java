package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DescriptionProductDTO {
    private Double weight;
    private String gender;
    private String brand;
    private String color;
    private String description;
}
