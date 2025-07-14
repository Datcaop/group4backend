package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipperInForDTO {
    private String address;
    private String email;
    private String phoneNumber;
    private String fullName;
}
