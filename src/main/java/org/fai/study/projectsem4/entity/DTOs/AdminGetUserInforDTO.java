package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminGetUserInforDTO {
    private Integer cusId;
    private String cusName;
    private String avatarUrl;
    private String email;
    private String phoneNumber;
    private String shippingAddress;
}
