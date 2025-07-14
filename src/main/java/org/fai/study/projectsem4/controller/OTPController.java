//package org.fai.study.projectsem4.controller;
//
//import org.fai.study.projectsem4.service.Impl.HunterService;
//import org.fai.study.projectsem4.service.interfaces.IOTPService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//public class OTPController {
//    @Autowired
//    IOTPService iotpService;
//    @Autowired
//    HunterService hunterService;
//
//    @PostMapping("/sendOTP")
//    public ResponseEntity<?> sendOTP(@RequestParam String email){
////        Check liệu email có tồn tại hay không ?
//        if (hunterService.validateExistingEmailByHunter(email)) {
//            try {
//                iotpService.generateAndSendOTP(email);
//                return ResponseEntity.ok("OTP sent to your email :" + email);
//            } catch (Exception e) {
//                return ResponseEntity.badRequest().body("Error sending OTP " + e.getMessage());
//            }
//        }else {
//            return ResponseEntity.badRequest().body("Can not find your email, please enter existing email!");
//        }
//    }
//
//    @PostMapping("/validate")
//    public ResponseEntity<?> validateOTP(@RequestParam String email, @RequestParam String otp){
//        boolean isValid = iotpService.validateOTP(email,otp);
//        if(isValid){
//            return ResponseEntity.ok("True OTP");
//        }else {
//            return ResponseEntity.badRequest().body("OTP is not valid");
//        }
//    }
//}
