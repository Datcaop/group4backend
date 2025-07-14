package org.fai.study.projectsem4.entity.DTOs;

import lombok.Builder;

public abstract class PaymentDTO {
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
        public Integer orderId;
        // Constructor thủ công
        public VNPayResponse(String code, String message, String paymentUrl,Integer orderId) {
            this.code = code;
            this.message = message;
            this.paymentUrl = paymentUrl;
            this.orderId = orderId;
        }
    }
}
