package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.*;
import org.fai.study.projectsem4.entity.DTOs.MailDTO;
import org.fai.study.projectsem4.entity.DTOs.ShippingRequestDTO;
import org.fai.study.projectsem4.entity.DTOs.ShippingResponseDTO;
import org.fai.study.projectsem4.entity.ENUM.EOrder;
import org.fai.study.projectsem4.entity.ENUM.EPayment;
import org.fai.study.projectsem4.entity.ENUM.EShip;
import org.fai.study.projectsem4.repository.OrderRepo;
import org.fai.study.projectsem4.repository.PaymentRepo;
import org.fai.study.projectsem4.repository.ShippingRepo;
import org.fai.study.projectsem4.repository.UserRepo;
import org.fai.study.projectsem4.service.interfaces.IS3Service;
import org.fai.study.projectsem4.service.interfaces.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShippingService implements IShippingService {
    @Autowired
    ShippingRepo shippingRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    OrderRepo orderRepo;

//    @Autowired
//    BlogStorageService blogStorageService;

    @Autowired
    IS3Service is3Service;
    @Autowired
    PaymentRepo paymentRepo;

    @Autowired
    MailServiceImpl mailService;

    @Override
    public ShippingResponseDTO addShipping(ShippingRequestDTO requestDTO) {

        Shipping aNewShipping = new Shipping();
        LocalDateTime now = LocalDateTime.now();
        aNewShipping.setTimeEstimate(now);
        aNewShipping.setStatus(EShip.RECEIVED);
        aNewShipping.setImage("");



//Lưu shipperID
        User shipper = userRepo.findById(requestDTO.getShipperId()).orElseThrow(()-> new RuntimeException("shipper not Found !"));

        aNewShipping.setShipper(shipper);
//Lưu orderID
        Order order = orderRepo.findById(requestDTO.getOrderId()).orElseThrow(()-> new RuntimeException("order not Found !"));
        aNewShipping.setOrder(order);

        LocalDateTime timeOrder = order.getOrderDate();
        DateTimeFormatter formatterTimeOrder = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        aNewShipping.setNotification("Admin has sent you an order, order time: "+timeOrder.format(formatterTimeOrder)+", please complete the order now!" );
//Lưu shipping
        shippingRepo.save(aNewShipping);


        try {
            //Lấy ra dữ liệu của shipping
            List<Object[]> shippingGetFromDB = shippingRepo.findShippingByOrderId(requestDTO.getOrderId());
            if (shippingGetFromDB == null) {
                throw new RuntimeException("Shipping not Found for Order ID: " + requestDTO.getOrderId());
            }
//Trar về dữ liệu
            ShippingResponseDTO responseDTO = new ShippingResponseDTO();
            Object[] shippingGetFromDB1 = shippingGetFromDB.get(0);
            Integer shippingId = (Integer) shippingGetFromDB1[0]; // Shipping ID
            Timestamp timestamp = (Timestamp) shippingGetFromDB1[1]; // Time estimate
            String shippingStatus = (String) shippingGetFromDB1[2];

            // Thiết lập các trường cho responseDTO
            responseDTO.setShippingId(shippingId);
            LocalDateTime time = timestamp.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            responseDTO.setTimeEstimate(time.format(formatter));
            responseDTO.setShippingStatus(shippingStatus);
            responseDTO.setOrderId(requestDTO.getOrderId());
            responseDTO.setShipperId(requestDTO.getShipperId());

            return responseDTO;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String shipperChangeOrderStatusIntoShipping(Integer shippingId) {
        Shipping shipping = shippingRepo.findById(shippingId).orElseThrow(() -> new RuntimeException("Shipping not Found!"));
        shipping.setStatus(EShip.SHIPPING);

        Order order = shipping.getOrder();
        order.setStatus(EOrder.SHIPPING);
        MailDTO mailDTO = new MailDTO();
        mailDTO.setSubject("Your Order is shipping !");
        mailDTO.setTo(order.getUser().getEmail());
        mailDTO.setContent("");
        Map<String, Object> shipperInformation = new HashMap<String,Object>();
        shipperInformation.put("invoiceId","INV-2025"+order.getOrderId());
        shipperInformation.put("shipperName",shipping.getShipper().getFullName());
        shipperInformation.put("phoneNumber",shipping.getShipper().getPhoneNumber());
        shipperInformation.put("licensePlate","88K1-64525");
        mailDTO.setProps(shipperInformation);





        shipping.setOrder(order);
        try {
            shippingRepo.save(shipping);
            mailService.sendEmail(mailDTO,"shipping-convert");
            return "Order status changed to SHIPPING!";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String shipperChangeOrderStatusIntoFinished(@RequestParam("shippingId") Integer shippingId,@RequestParam("image") MultipartFile image) {
        Shipping shipping = shippingRepo.findById(shippingId).orElseThrow(() -> new RuntimeException("Shipping not Found!"));
        shipping.setStatus(EShip.FINISHED);

        try {
            String fileUrl1 = is3Service.uploadFile(image);
            shipping.setImage(fileUrl1);
            Order order = shipping.getOrder();
            order.setStatus(EOrder.FINISHED);

            Payment payment = paymentRepo.findByOrder_OrderId(order.getOrderId());


            if (payment == null) {
                throw new RuntimeException("Payment not found for Order ID: " + order.getOrderId());
            }
            payment.setStatus(EPayment.PAID);
            payment.setPaidMoney(order.getTotalPrice());

            order.setPayment(payment);

            shipping.setOrder(order);

            shippingRepo.save(shipping);
            MailDTO mailDTO = new MailDTO();
            mailDTO.setSubject("Thanks for choosing us !");
            mailDTO.setTo(order.getUser().getEmail());
            mailDTO.setContent("");
            mailService.sendEmail(mailDTO,"finished-template");
            return "Order status changed to FINISHED!";
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Shipping shipperChangeOrderStatusIntoNotAccepted(Integer shippingId) {
        Shipping shipping = shippingRepo.findById(shippingId).orElseThrow(() -> new RuntimeException("Shipping not Found!"));
        if (shipping.getStatus() == EShip.RECEIVED) {
            return null;
        } else if (shipping.getStatus() == EShip.FINISHED) {
            return null;
        }
        try {
        shipping.setStatus(EShip.NOT_ACCEPTED);

        Order order = shipping.getOrder();
        order.setStatus(EOrder.RETURN_ORDER);

        MailDTO mailDTO = new MailDTO();
        mailDTO.setSubject("Return order!");
        mailDTO.setTo(order.getUser().getEmail());
        mailDTO.setContent("");
        mailService.sendEmail(mailDTO,"return-order");

        shipping.setOrder(order);

            return shippingRepo.save(shipping);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
