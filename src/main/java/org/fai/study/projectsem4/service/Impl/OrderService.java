package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.*;
import org.fai.study.projectsem4.entity.DTOs.*;
import org.fai.study.projectsem4.entity.ENUM.EOrder;
import org.fai.study.projectsem4.entity.ENUM.EPayment;
import org.fai.study.projectsem4.repository.*;
import org.fai.study.projectsem4.service.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailRepo orderDetailRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private PaymentMethodRepo paymentMethodRepo;

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private ImageRepo imageRepo;


    @Override
    public String addNewOrder(OrderDTO orderDTO,Integer userId) {
        // Lấy thông tin User
        Optional<User> optionalUser = userRepo.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        User user = optionalUser.get();
        try{
            // Tạo mới Order
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(EOrder.PENDING); // Trạng thái mặc định
            order.setTotalPrice(orderDTO.getTotalPrice());
            order.setAddress(orderDTO.getAddress());
            order.setPhoneNumber(orderDTO.getPhoneNumber());

            // Liên kết với User
            user.getOrders().add(order);

            Order savedOrder = orderRepo.save(order);

//            Tạo payment cho thanh toán COD
            Payment CODpayment = new Payment();
            CODpayment.setOrder(savedOrder);

            PaymentMethod CODpaymentMethod = paymentMethodRepo.findById(1)
                    .orElseThrow(() -> new RuntimeException("Payment method not found"));
            CODpayment.setPaymentMethod(CODpaymentMethod);
            CODpayment.setStatus(EPayment.NOT_PAY);
            CODpayment.setCreatedAt(LocalDateTime.now());
            CODpayment.setUpdatedAt(LocalDateTime.now());
            CODpayment.setPaidMoney(0.0);

            paymentRepo.save(CODpayment);
            MailDTO mailDTO = new MailDTO();
            mailDTO.setTo(user.getEmail());
            mailDTO.setSubject("Order successful!");
            mailDTO.setContent("");
            Map<String, Object> invoice = new HashMap<String,Object>();
            invoice.put("Customer",user.getFullName());
            invoice.put("InvoiceNumber","INV-2025"+savedOrder.getOrderId());

            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            invoice.put("time",time.format(formatter));
            invoice.put("total",savedOrder.getTotalPrice());

            List<Map<String, Object>> orderDetailsMap = new ArrayList<>();

            for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetails()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(orderDetailDTO.getQuantity());
                orderDetail.setSize(orderDetailDTO.getSize());
                orderDetail.setPrice(orderDetailDTO.getPriceAtOrder());

                Product product = productRepo.findById(orderDetailDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                product.setSold(product.getSold()+1);
                orderDetail.setProduct(product);



                // Liên kết với Order
                orderDetail.setOrder(savedOrder);
                savedOrder.getOrderDetails().add(orderDetail);

                orderDetailRepo.save(orderDetail);
                // Lấy hình ảnh sản phẩm
                Optional<Image> productImage = imageRepo.findFirstByProduct_ProductId(orderDetailDTO.getProductId());
                String imageUrl = productImage.map(Image::getUrl).orElse("");
//                Cho detail vào Map
                Map<String, Object> detail = new HashMap<>();
                detail.put("productName", productRepo.findById(orderDetailDTO.getProductId()).get().getProductName());
                detail.put("size", orderDetail.getSize());
                detail.put("quantity", orderDetail.getQuantity());
                detail.put("price", orderDetail.getPrice());
                detail.put("imageUrl", imageUrl);
                orderDetailsMap.add(detail);

            }
            invoice.put("invoiceDetails", orderDetailsMap);
            mailDTO.setProps(invoice);
            mailService.sendEmail(mailDTO,"order-email");
            return "Order created successfully!";
        }catch (Exception e){
            e.printStackTrace();
            return "Failed when created Order!";
        }

    }

    @Override
    public String addNewVnPayOrder(OrderDTO orderDTO, Integer userId) {
        // Lấy thông tin User
        Optional<User> optionalUser = userRepo.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        User user = optionalUser.get();
        try{
            // Tạo mới Order
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(EOrder.PENDING); // Trạng thái mặc định
            order.setTotalPrice(orderDTO.getTotalPrice());
            order.setAddress(orderDTO.getAddress());
            order.setPhoneNumber(orderDTO.getPhoneNumber());

            // Liên kết với User
            user.getOrders().add(order);

            Order savedOrder = orderRepo.save(order);

//            Tạo payment cho thanh toán COD
            Payment VnPayPayment = new Payment();
            VnPayPayment.setOrder(savedOrder);

            PaymentMethod CODpaymentMethod = paymentMethodRepo.findById(2)
                    .orElseThrow(() -> new RuntimeException("Payment method not found"));
            VnPayPayment.setPaymentMethod(CODpaymentMethod);
            VnPayPayment.setStatus(EPayment.PAID);
            VnPayPayment.setCreatedAt(LocalDateTime.now());
            VnPayPayment.setUpdatedAt(LocalDateTime.now());
            VnPayPayment.setPaidMoney(orderDTO.getTotalPrice());

            paymentRepo.save(VnPayPayment);
            MailDTO mailDTO = new MailDTO();
            mailDTO.setTo(user.getEmail());
            mailDTO.setSubject("Order successful!");
            mailDTO.setContent("");
            Map<String, Object> invoice = new HashMap<String,Object>();
            invoice.put("Customer",user.getFullName());
            invoice.put("InvoiceNumber","INV-2025"+savedOrder.getOrderId());

            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            invoice.put("time",time.format(formatter));
            invoice.put("total",savedOrder.getTotalPrice());

            List<Map<String, Object>> orderDetailsMap = new ArrayList<>();

            for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetails()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(orderDetailDTO.getQuantity());
                orderDetail.setSize(orderDetailDTO.getSize());
                orderDetail.setPrice(orderDetailDTO.getPriceAtOrder());

                Product product = productRepo.findById(orderDetailDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                product.setSold(product.getSold()+1);
                orderDetail.setProduct(product);



                // Liên kết với Order
                orderDetail.setOrder(savedOrder);
                savedOrder.getOrderDetails().add(orderDetail);

                orderDetailRepo.save(orderDetail);
                // Lấy hình ảnh sản phẩm
                Optional<Image> productImage = imageRepo.findFirstByProduct_ProductId(orderDetailDTO.getProductId());
                String imageUrl = productImage.map(Image::getUrl).orElse("");
//                Cho detail vào Map
                Map<String, Object> detail = new HashMap<>();
                detail.put("productName", productRepo.findById(orderDetailDTO.getProductId()).get().getProductName());
                detail.put("size", orderDetail.getSize());
                detail.put("quantity", orderDetail.getQuantity());
                detail.put("price", orderDetail.getPrice());
                detail.put("imageUrl", imageUrl);
                orderDetailsMap.add(detail);

            }
            invoice.put("invoiceDetails", orderDetailsMap);
            mailDTO.setProps(invoice);
            mailService.sendEmail(mailDTO,"order-email");
            return "Order created successfully!";
        }catch (Exception e){
            e.printStackTrace();
            return "Failed when created Order!";
        }
    }

    @Override
    public List<AdminOrderResponseDTO> getAllOrderAdmin() {
        List<Object[]> ordersGetFromDB = orderRepo.getAllOrder();
        List<AdminOrderResponseDTO> adminOrderResponseDTOList = new ArrayList<AdminOrderResponseDTO>();
        for (Object[] orderFromDb : ordersGetFromDB){
            AdminOrderResponseDTO orderDTO = new AdminOrderResponseDTO();
            orderDTO.setOrderId((Integer) orderFromDb[0]);
            orderDTO.setCustomerName((String) orderFromDb[1]);
            orderDTO.setAvatar("https://414500107011.s3.ap-southeast-2.amazonaws.com/cr7.jpeg");
            orderDTO.setAddress((String) orderFromDb[2]);

            Timestamp timestamp = (Timestamp) orderFromDb[3];
            LocalDateTime time = timestamp.toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fomartDateTime = time.format(formatter);
            orderDTO.setOrderDate(fomartDateTime);

            orderDTO.setOrderPrice((Double) orderFromDb[4]);
            orderDTO.setStatus((String) orderFromDb[5]);

            adminOrderResponseDTOList.add(orderDTO);
        }
        return adminOrderResponseDTOList;
    }


    @Override
    public AdminOrderDetailResponseDTO getOrderDetailInforAdminByOrderId(Integer orderId) {
        List<Object[]> orderDetailGetFromDB = orderRepo.getOrderDetailByOrderIdAdmin(orderId);
        Object[] orderDetailFromDB1 = orderDetailGetFromDB.get(0);
        AdminOrderDetailResponseDTO orderDetailDTO = new AdminOrderDetailResponseDTO();


        orderDetailDTO.setOrderId((Integer) orderDetailFromDB1[0]);

        Timestamp timestamp = (Timestamp) orderDetailFromDB1[1];
        LocalDateTime time = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fomartDateTime = time.format(formatter);
        orderDetailDTO.setOrderDate(fomartDateTime);

        List<AdminOrderDetailsDTO> orderDetailsDTOList = new ArrayList<>();
        for (Object[] data : orderDetailGetFromDB){
                    AdminOrderDetailsDTO adminOrderDetailsDTO = new AdminOrderDetailsDTO();
                    adminOrderDetailsDTO.setOrderDetailId((Integer) data[2]);
                    adminOrderDetailsDTO.setImage((String)data[3] );
                    adminOrderDetailsDTO.setProductName((String) data[4]);
                    adminOrderDetailsDTO.setSku((String) data[5]);
                    adminOrderDetailsDTO.setOrderDetailPrice((Double) data[6]);
                    adminOrderDetailsDTO.setQuantity((Integer) data[7]);

                    orderDetailsDTOList.add(adminOrderDetailsDTO);



        }
        orderDetailDTO.setOrderDetails(orderDetailsDTOList);

        orderDetailDTO.setTotalPrice((Double)orderDetailFromDB1[8]);
        orderDetailDTO.setPaymentMethod((String) orderDetailFromDB1[9]);
        orderDetailDTO.setPaymentStatus((String) orderDetailFromDB1[10]);
        orderDetailDTO.setPaidMoney((Double) orderDetailFromDB1[11]);

        //Lấy dữ liệu add vào userInfor
        AdminGetUserInforDTO userInforDTO = new AdminGetUserInforDTO();
        userInforDTO.setCusId((Integer) orderDetailFromDB1[12]);
        userInforDTO.setCusName((String) orderDetailFromDB1[13]);
        userInforDTO.setAvatarUrl("https://414500107011.s3.ap-southeast-2.amazonaws.com/cr7.jpeg");
        userInforDTO.setEmail((String) orderDetailFromDB1[14]);
        userInforDTO.setPhoneNumber((String) orderDetailFromDB1[15]);
        userInforDTO.setShippingAddress((String) orderDetailFromDB1[16]);
        //Lấy dữ liệu add vào shippingInfor
        ShippingDTO shippingDTO = new ShippingDTO();
        shippingDTO.setShippingId((Integer) orderDetailFromDB1[17]);
        shippingDTO.setShippingStatus((String) orderDetailFromDB1[18]);
        shippingDTO.setImage((String) orderDetailFromDB1[19]);


        orderDetailDTO.setCustomerInformation(userInforDTO);
        orderDetailDTO.setShippingInformation(shippingDTO);

        return orderDetailDTO;
    }

    @Override
    public List<MonthRevenueDashboardDTO> getMonthRevenueDashboard() {
        List<Object[]> orderMonthlyDashboard = orderRepo.getOrderMonthlyDashboard();
        List<MonthRevenueDashboardDTO> monthRevenueDashboardDTOList = new ArrayList<>();
        for (Object[] data : orderMonthlyDashboard){
            MonthRevenueDashboardDTO monthRevenueDashboardDTO = new MonthRevenueDashboardDTO();
            monthRevenueDashboardDTO.setMonth((String) data[0]);
            monthRevenueDashboardDTO.setTotalOrder(((Long) data[1]).intValue());
            monthRevenueDashboardDTO.setTotalRevenue((Double) data[2]);

            monthRevenueDashboardDTOList.add(monthRevenueDashboardDTO);
        }
        monthRevenueDashboardDTOList.add(new MonthRevenueDashboardDTO("11-2024",21,20000.00));
        monthRevenueDashboardDTOList.add(new MonthRevenueDashboardDTO("10-2024",19,15000.00));
        monthRevenueDashboardDTOList.add(new MonthRevenueDashboardDTO("09-2024",32,57120.00));
        monthRevenueDashboardDTOList.add(new MonthRevenueDashboardDTO("08-2024",27,43000.00));
        return monthRevenueDashboardDTOList;
    }

    @Override
    public List<StatusOrderDashboardDTO> getStatusOrderDashboard() {
        List<Object[]> orderStatusDashboard = orderRepo.getStatusOrderDashboard();
        List<StatusOrderDashboardDTO> statusOrderDashboardDTOList = new ArrayList<>();
        for (Object[] data : orderStatusDashboard){
            StatusOrderDashboardDTO statusOrderDashboardDTO = new StatusOrderDashboardDTO();
            statusOrderDashboardDTO.setOrderStatus((String) data[0]);
            statusOrderDashboardDTO.setTotalOrders(((Long) data[1]).intValue());

            statusOrderDashboardDTOList.add(statusOrderDashboardDTO);
        }
        return statusOrderDashboardDTOList;
    }

    @Override
    public List<UserRoleDTO> getUserRoleDashboard() {
        List<Object[]> userRoleDashboard = userRepo.countNumberOfUserByRole();
        List<UserRoleDTO> userRoleDTOList = new ArrayList<>();
        for (Object[] data : userRoleDashboard){
            UserRoleDTO userRoleDTO = new UserRoleDTO();
            userRoleDTO.setRole((String) data[0]);
            userRoleDTO.setTotalUsers(((Long) data[1]).intValue());

            userRoleDTOList.add(userRoleDTO);
        }
        return userRoleDTOList;
    }

    @Override
    public String tranferOrderStatus(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        Payment fixPayment = order.getPayment();
        if (fixPayment.getStatus().equals(EPayment.PAID)) {
            return "Order status is already paid!";
        }
        fixPayment.setStatus(EPayment.PAID);

        order.setPayment(fixPayment);

        orderRepo.save(order);
        return "Order status updated successfully!";
    }

    @Override
    public List<AdminOrderResponseDTO> getAllOrderUser(Integer userId) {
        List<Object[]> ordersGetFromDB = orderRepo.getAllOrderUser(userId);
        List<AdminOrderResponseDTO> adminOrderResponseDTOList = new ArrayList<AdminOrderResponseDTO>();
        for (Object[] orderFromDb : ordersGetFromDB){
            AdminOrderResponseDTO orderDTO = new AdminOrderResponseDTO();
            orderDTO.setOrderId((Integer) orderFromDb[0]);
            orderDTO.setCustomerName((String) orderFromDb[1]);
            orderDTO.setAvatar("https://group4blog.blob.core.windows.net/avatar/avtar.jpg");
            orderDTO.setAddress((String) orderFromDb[2]);

            Timestamp timestamp = (Timestamp) orderFromDb[3];
            LocalDateTime time = timestamp.toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fomartDateTime = time.format(formatter);
            orderDTO.setOrderDate(fomartDateTime);

            orderDTO.setOrderPrice((Double) orderFromDb[4]);
            orderDTO.setStatus((String) orderFromDb[5]);

            adminOrderResponseDTOList.add(orderDTO);
        }
        return adminOrderResponseDTOList;
    }

}
