package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fai.study.projectsem4.entity.ENUM.EOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private String fullName;
    private String email;
    private Double totalPrice;
    private String address;
    private String phoneNumber;
    private List<OrderDetailDTO> orderDetails;
}
