package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.DTOs.NotificationDTO;
import org.fai.study.projectsem4.entity.DTOs.OrdersShipperDTO;
import org.fai.study.projectsem4.entity.DTOs.ShipperInforWithStatusDTO;
import org.fai.study.projectsem4.entity.ENUM.EShipperStatus;
import org.fai.study.projectsem4.entity.Shipping;
import org.fai.study.projectsem4.entity.User;
import org.fai.study.projectsem4.repository.OrderRepo;
import org.fai.study.projectsem4.repository.ShippingRepo;
import org.fai.study.projectsem4.repository.UserRepo;
import org.fai.study.projectsem4.service.interfaces.IShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShipperService implements IShipperService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    ShippingRepo shippingRepo;
    @Autowired
    OrderRepo orderRepo;
    @Override
    public List<ShipperInforWithStatusDTO> getAllShipperInfo() {
        List<ShipperInforWithStatusDTO> listShipperInfor = new ArrayList<ShipperInforWithStatusDTO>();

        List<Object[]> getShipperInforFromDB = userRepo.getAllShipperInforWithStatus();
        for(Object[] data : getShipperInforFromDB) {
            ShipperInforWithStatusDTO shipperInfo = new ShipperInforWithStatusDTO();

            shipperInfo.setShipperId((Integer) data[0]);
            shipperInfo.setShipperName((String) data[1]);
            shipperInfo.setShipperPhoneNumber((String) data[2]);
            shipperInfo.setShipperEmail((String) data[3]);
            shipperInfo.setAvatar((String) data[4]);
            shipperInfo.setShipperStatus(EShipperStatus.valueOf((String)data[5] ));

            listShipperInfor.add(shipperInfo);

        }
        return listShipperInfor;
    }

    @Override
    public List<NotificationDTO> notifyShipper(Integer shipperId) {
        User user = userRepo.findById(shipperId).orElseThrow(()->new RuntimeException("shipper not found !"));

        List<Shipping> shippingList = shippingRepo.findByShipper_UserId(shipperId);
        List<NotificationDTO> notificationDTOList = new ArrayList<>();

        for(Shipping shipping : shippingList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setShippingId(shipping.getShipId());
            notificationDTO.setMessage(shipping.getNotification());

            notificationDTOList.add(notificationDTO);
        }
        return notificationDTOList;
    }

    @Override
    public List<OrdersShipperDTO> getALLOrdersOfShipperByShipperId(Integer shipperId) {
        User user = userRepo.findById(shipperId).orElseThrow(()->new RuntimeException("shipper not found !"));
        List<Object[]> dataGetFromDB = orderRepo.getAllOrderByShipperId(shipperId);
        List<OrdersShipperDTO> ordersShipperDTOList = new ArrayList<>();

        for (Object[] data : dataGetFromDB) {
            OrdersShipperDTO oSD = new OrdersShipperDTO();
            oSD.setOrderId((Integer) data[0]);
            oSD.setShippingId((Integer) data[1]);
            oSD.setCustomerName((String) data[2]);
            oSD.setPhoneNumber((String) data[3]);
            oSD.setAddress((String)data[4]);
            Timestamp timestamp = (Timestamp) data[5];
            LocalDateTime time = timestamp.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            oSD.setOrderDate(time.format(formatter));
            oSD.setTotalPrice((Double) data[6]);
            oSD.setStatus((String) data[7]);

            ordersShipperDTOList.add(oSD);
        }
        return ordersShipperDTOList;
    }
}
