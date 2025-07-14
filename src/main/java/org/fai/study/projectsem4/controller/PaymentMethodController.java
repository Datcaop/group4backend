package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.entity.PaymentMethod;
import org.fai.study.projectsem4.repository.PaymentMethodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class PaymentMethodController {
    @Autowired
    PaymentMethodRepo paymentMethodRepo;
    @PostMapping("/payment-method")
    public ResponseEntity<?> addPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        if (paymentMethod.getMethodName() == null || paymentMethod.getMethodName().isEmpty()) {
            return ResponseEntity.badRequest().body("Payment method name cannot be empty");
        }
        PaymentMethod savedPaymentMethod = paymentMethodRepo.save(paymentMethod);
        return ResponseEntity.ok(savedPaymentMethod);
    }
}
