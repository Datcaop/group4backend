package org.fai.study.projectsem4.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.fai.study.projectsem4.configuration.JWTUtils;
import org.fai.study.projectsem4.entity.DTOs.OrderDTO;
import org.fai.study.projectsem4.entity.DTOs.PaymentDTO;
import org.fai.study.projectsem4.entity.DTOs.ResponseObject;
import org.fai.study.projectsem4.service.Impl.PaymentService;
import org.fai.study.projectsem4.service.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    IOrderService orderService;
    @GetMapping("/v1/payment/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }
    @PostMapping("/v2/payment/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> callVnPayRequest(HttpServletRequest request,@RequestBody OrderDTO orderDTO,@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserIdFromJwtToken(token);
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPaymentFix(request,orderDTO,userId));
    }
    @PostMapping("/auth/v1/tranferOrderStatus")
    public ResponseEntity<String> tranferOrderStatus(@RequestParam Integer orderId) {
        String result = orderService.tranferOrderStatus(orderId);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
//    @GetMapping("/auth/v1/vn-pay-callback")
//    public ResponseObject<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
//        String status = request.getParameter("vnp_ResponseCode");
//        if (status.equals("00")) {
//            return new ResponseObject<>(HttpStatus.OK, "Success", new PaymentDTO.VNPayResponse("00", "Success","", 20));
//        } else {
//            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
//        }
//    }
//    @GetMapping("/v1/insertOrderToDBVnPay")
//    public String payCallbackHandler( @RequestBody OrderDTO orderDTO, @RequestHeader("Authorization") String authHeader) {
//        String token = authHeader.substring(7);
//        Integer userId = jwtUtils.getUserIdFromJwtToken(token);
//        return orderService.addNewVnPayOrder(orderDTO,userId);
//    }
}
