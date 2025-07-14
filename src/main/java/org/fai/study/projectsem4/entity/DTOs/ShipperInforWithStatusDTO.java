package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fai.study.projectsem4.entity.ENUM.EShipperStatus;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipperInforWithStatusDTO {
    private Integer shipperId;
    private String shipperName;
    private String shipperPhoneNumber;
    private String shipperEmail;
    private String avatar;
    private EShipperStatus shipperStatus;
}
