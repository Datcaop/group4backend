package org.fai.study.projectsem4.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fai.study.projectsem4.configuration.JWTUtils;
import org.fai.study.projectsem4.entity.DTOs.*;
import org.fai.study.projectsem4.entity.Image;
import org.fai.study.projectsem4.repository.ImageRepo;
import org.fai.study.projectsem4.service.Impl.OrderService;
import org.fai.study.projectsem4.service.Impl.ShipperService;
import org.fai.study.projectsem4.service.Impl.ShippingService;
import org.fai.study.projectsem4.service.interfaces.IS3Service;
import org.fai.study.projectsem4.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/shipper")
public class ShipperController {

    @Autowired
    ShippingService shippingService;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    ShipperService shipperService;

    @Autowired
    OrderService orderService;

    @Autowired
    IUserService userService;

//    @Autowired
//    BlogStorageService blogStorageService;
    @Autowired
    IS3Service is3Service;

    @Autowired
    ImageRepo imageRepo;

//order cua shipper
    @PreAuthorize("hasRole('SHIPPER')")
    @PostMapping("/convertShippingStatus")
    public ResponseEntity<?> convertShippingStatus(@RequestParam Integer shippingId) {
        if (shippingService.shipperChangeOrderStatusIntoShipping(shippingId) != null){
            return ResponseEntity.ok("Shipping status changed to " + shippingId);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SHIPPER')")
    @PostMapping("/convertFinishedStatus")
    public ResponseEntity<?> convertFinishedStatus(@RequestParam("shippingId") Integer shippingId, @RequestParam("image") MultipartFile image) {
        if (shippingService.shipperChangeOrderStatusIntoFinished(shippingId,image) != null){
            return ResponseEntity.ok("Shipping finished: " + shippingId);
        }
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('SHIPPER')")
    @PostMapping("/convertNotAcceptStatus")
    public ResponseEntity<?> convertFinishedStatus(@RequestParam Integer shippingId) {
        if (shippingService.shipperChangeOrderStatusIntoNotAccepted(shippingId) != null){
            return ResponseEntity.ok("Shipping not succeed: " + shippingId);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SHIPPER')")
    @GetMapping("/getNotificationForShipper")
    public ResponseEntity<List<NotificationDTO>> getNotificationForShipper(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer shipperId = jwtUtils.getUserIdFromJwtToken(token);
        List<NotificationDTO> notificationDTOList= shipperService.notifyShipper(shipperId);
        if (notificationDTOList != null){
            return ResponseEntity.ok(notificationDTOList);
        }return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('SHIPPER')")
    @GetMapping("/getAllOrderOfShipper")
    public ResponseEntity<List<OrdersShipperDTO>> getAllOrderOfShipper(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        Integer shipperId = jwtUtils.getUserIdFromJwtToken(token);
        List<OrdersShipperDTO> ordersShipperDTOList = shipperService.getALLOrdersOfShipperByShipperId(shipperId);
        if (ordersShipperDTOList != null){
            return ResponseEntity.ok(ordersShipperDTOList);
        }return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SHIPPER')")
    @GetMapping("/getOrderDetailByOrderIdShipper")
    public ResponseEntity<AdminOrderDetailResponseDTO> shipperOrderDetail(@RequestParam Integer orderId) {
        AdminOrderDetailResponseDTO adminOrderDetailResponseDTO = orderService.getOrderDetailInforAdminByOrderId(orderId);
        if (adminOrderDetailResponseDTO != null){
            return ResponseEntity.ok(adminOrderDetailResponseDTO);
        }else {
            return ResponseEntity.noContent().build();
        }
    }

//     thong tin shipper
    @PreAuthorize("hasRole('SHIPPER')")
    @GetMapping("/getShipperInforByShipperId")
    public ResponseEntity<UserDTO> getShipperInforByShipperId(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        Integer shipperId = jwtUtils.getUserIdFromJwtToken(token);
        UserDTO userDTO = userService.getShipperByShipperId(shipperId);
        if (userDTO != null){
            return ResponseEntity.ok(userDTO);
        }return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SHIPPER')")
    @PostMapping("/changeShipperInforByShipperId")
    public ResponseEntity<ShipperInForDTO> updateShipperInforByShipperId(@RequestHeader("Authorization") String authHeader,@RequestParam("avtImage") MultipartFile avtImage,@RequestParam("shipperInForDTOString") String shipperInForDTOString){
        String token = authHeader.substring(7);
        Integer shipperId = jwtUtils.getUserIdFromJwtToken(token);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ShipperInForDTO shipperInForDTO = objectMapper.readValue(shipperInForDTOString, ShipperInForDTO.class);
            String fileUrl = is3Service.uploadFile(avtImage);

            Image image = new Image();
            image.setUrl(fileUrl);
            imageRepo.save(image);
            ShipperInForDTO shipperInForDTO1 = userService.updateShipperInformation(shipperId,shipperInForDTO,fileUrl);
            if (shipperInForDTO1 != null){
                return ResponseEntity.ok(shipperInForDTO1);
            }return ResponseEntity.noContent().build();
        }catch (Exception e){
        e.printStackTrace();
        return ResponseEntity.noContent().build();
        }
    }


}
