package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.DTOs.MailDTO;
import org.fai.study.projectsem4.service.interfaces.IOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService implements IOTPService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private HunterService hunterService;
    @Autowired
    MailServiceImpl mailService;

    private static long OTP_EXPIRATION_MINUTES = 10;
    @Override
    public String generateAndSendOTP(String email) {
        try {
//        Tạo mã OTP
            String otp = generateOTPCode(6);
                //        Lưu OTP vào Redis với TTL
//                redisTemplate.opsForValue().set(email, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
//    Gửi email
            MailDTO mailDTO = new MailDTO();
            mailDTO.setSubject("Verify Account!");
            mailDTO.setTo(email);
            mailDTO.setContent("");
            Map<String, Object> otpInformation = new HashMap<String,Object>();
            otpInformation.put("otp",otp);
            mailDTO.setProps(otpInformation);

            mailService.sendEmail(mailDTO,"otp-template");
            return otp;


        }catch (Exception e){
            e.printStackTrace();
            return "Failed to generate OTP code";
        }
    }

    @Override
    public boolean validateOTP(String email, String otp) {


        String storedOtp = redisTemplate.opsForValue().get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            // Xóa OTP sau khi xác thực thành công
            redisTemplate.delete(email);
            return true;
        }
        return false;

    }

    public void sendOTPEmail(String email, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\nThis code is valid for " + OTP_EXPIRATION_MINUTES + " minutes.");
        mailSender.send(message);
    }

    public String generateOTPCode(Integer length){
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
