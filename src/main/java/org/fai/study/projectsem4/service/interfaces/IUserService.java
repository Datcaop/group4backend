package org.fai.study.projectsem4.service.interfaces;

import org.fai.study.projectsem4.entity.DTOs.ShipperInForDTO;
import org.fai.study.projectsem4.entity.DTOs.UserDTO;
import org.fai.study.projectsem4.entity.DTOs.UserInforDTO;

import java.util.List;

public interface IUserService {
    UserInforDTO getUserInfor(Integer userId);
    List<UserDTO> getAllInforUserWithRole();
    UserDTO getShipperByShipperId(Integer shipperId);
    ShipperInForDTO updateShipperInformation(Integer shipperId,ShipperInForDTO shipperInForDTO,String avtUrl);

}
