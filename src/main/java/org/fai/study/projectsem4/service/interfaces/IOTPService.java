package org.fai.study.projectsem4.service.interfaces;

public interface IOTPService {
    String generateAndSendOTP(String email);
    boolean validateOTP(String email, String otp);
}
