package org.fai.study.projectsem4.service.Impl;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.fai.study.projectsem4.configuration.VNPayConfig;
import org.fai.study.projectsem4.configuration.VnPayUtil;
import org.fai.study.projectsem4.entity.*;
import org.fai.study.projectsem4.entity.DTOs.MailDTO;
import org.fai.study.projectsem4.entity.DTOs.OrderDTO;
import org.fai.study.projectsem4.entity.DTOs.OrderDetailDTO;
import org.fai.study.projectsem4.entity.DTOs.PaymentDTO;
import org.fai.study.projectsem4.entity.ENUM.EOrder;
import org.fai.study.projectsem4.entity.ENUM.EPayment;
import org.fai.study.projectsem4.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final PaymentRepo paymentRepo;
    private final ProductRepo productRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final MailServiceImpl mailService;
    private final ImageRepo imageRepo;
    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
//        vnpParamsMap.put("vnp_IpAddr", VnPayUtil.getIpAddress(request));
//        String clientIp = VnPayUtil.getIpAddress(request);
//        if (VnPayUtil.getIpAddress(request).equals("0:0:0:0:0:0:0:1")) {
//                clientIp = "127.0.0.1";
//        }
        String clientIp = "35.192.217.222"; // IP của server
//        String clientIp = "127.0.0.1"; // IP của localhost
        vnpParamsMap.put("vnp_IpAddr",clientIp);
        //build query url
        String queryUrl = VnPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VnPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VnPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    public PaymentDTO.VNPayResponse createVnPayPaymentFix(HttpServletRequest request, OrderDTO orderDTO, Integer userId) {
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
            VnPayPayment.setStatus(EPayment.NOT_PAY);
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
// call-vn-pay
            long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
            String bankCode = request.getParameter("bankCode");
            Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
            vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
            if (bankCode != null && !bankCode.isEmpty()) {
                vnpParamsMap.put("vnp_BankCode", bankCode);
            }
//        vnpParamsMap.put("vnp_IpAddr", VnPayUtil.getIpAddress(request));
            String clientIp = VnPayUtil.getIpAddress(request);
            if (VnPayUtil.getIpAddress(request).equals("0:0:0:0:0:0:0:1")) {
                clientIp = "127.0.0.1";
            }
//            String clientIp = "35.192.217.222"; // IP của server
//        String clientIp = "127.0.0.1"; // IP của localhost
            vnpParamsMap.put("vnp_IpAddr",clientIp);
            //build query url
            String queryUrl = VnPayUtil.getPaymentURL(vnpParamsMap, true);
            String hashData = VnPayUtil.getPaymentURL(vnpParamsMap, false);
            String vnpSecureHash = VnPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
            queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
            String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
            return PaymentDTO.VNPayResponse.builder()
                    .code("ok")
                    .message("success")
                    .paymentUrl(paymentUrl)
                    .orderId(savedOrder.getOrderId())
                    .build();


        }catch (Exception e){
            e.printStackTrace();
            return PaymentDTO.VNPayResponse.builder()
                    .code("not ok")
                    .message("failed")
                    .paymentUrl("NoURL")
                    .orderId(0)
                    .build();
        }

    }
}
