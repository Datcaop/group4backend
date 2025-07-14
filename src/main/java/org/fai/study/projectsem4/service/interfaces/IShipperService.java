package org.fai.study.projectsem4.service.interfaces;

import org.fai.study.projectsem4.entity.DTOs.NotificationDTO;
import org.fai.study.projectsem4.entity.DTOs.OrdersShipperDTO;
import org.fai.study.projectsem4.entity.DTOs.ShipperInforWithStatusDTO;

import java.util.List;

public interface IShipperService {
    List<ShipperInforWithStatusDTO> getAllShipperInfo();
    List<NotificationDTO> notifyShipper(Integer shipperId);
    List<OrdersShipperDTO> getALLOrdersOfShipperByShipperId(Integer shipperId);
}
