package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.convert.DataSizeUnit;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartResultDTO {
    private String message;
    private Integer code;
}
