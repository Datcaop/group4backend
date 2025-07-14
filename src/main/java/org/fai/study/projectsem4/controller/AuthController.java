package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.configuration.JWTUtils;
import org.fai.study.projectsem4.entity.DTOs.*;
import org.fai.study.projectsem4.entity.ENUM.ERole;
import org.fai.study.projectsem4.entity.Role;
import org.fai.study.projectsem4.entity.User;
import org.fai.study.projectsem4.entity.UserDetailsImpl;
import org.fai.study.projectsem4.repository.RoleRepo;
import org.fai.study.projectsem4.repository.UserRepo;
import org.fai.study.projectsem4.service.Impl.HunterService;
import org.fai.study.projectsem4.service.interfaces.IOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepo userRepository;

    @Autowired
    RoleRepo roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    IOTPService iotpService;

    @Autowired
    HunterService hunterService;

    @Autowired
    RedisTemplate<String,SignUpOTPRequest > redisTemplate;

    private static final long SIGNUP_EXPIRATION_MINUTES = 10;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String roleName = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(new LoginResponseDTO(jwt, roleName));

    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDTO signUpRequest) {
//        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
//            return ResponseEntity.badRequest().body("Username is already taken!");
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body("Email is already in use!");
//        }
//
//        // Kiểm tra email hợp lệ bằng HunterService
////        if (!hunterService.validateExistingEmailByHunter(signUpRequest.getEmail())) {
////            return ResponseEntity.badRequest().body("Invalid email address, please enter existing email!");
////        }
////Map sang object có otp để lưu
//        SignUpOTPRequest OTPRequest = new SignUpOTPRequest();
//        OTPRequest.userName = signUpRequest.getUserName();
//        OTPRequest.email= signUpRequest.getEmail();
//        OTPRequest.password = signUpRequest.getPassword();
//        OTPRequest.fullName = signUpRequest.getFullName();
//        OTPRequest.role = signUpRequest.getRole();
//        // Gửi OTP
//        OTPRequest.otp = iotpService.generateAndSendOTP(signUpRequest.getEmail());
//
//        // Lưu thông tin đăng ký tạm thời vào Redis
//        redisTemplate.opsForValue().set(signUpRequest.getEmail(), OTPRequest, SIGNUP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
//
//
//
//        return ResponseEntity.ok("OTP sent to your email. Please verify to complete registration.");
//    }
//
//    @PostMapping("/verifyOTP")
//    public ResponseEntity<?> verifyOTP(@RequestParam String email, @RequestParam String otp) {
//        // Kiểm tra OTP
//        try {
//            // Lấy thông tin đăng ký tạm thời từ Redis
//            SignUpOTPRequest signupRequestDTO = redisTemplate.opsForValue().get(email);
////                Gán vào signuprequestDto để lưu vào DB
//            SignupRequestDTO signUpRequest = new SignupRequestDTO();
//            signUpRequest.userName= signupRequestDTO.userName;
//            signUpRequest.email= signupRequestDTO.email;;
//            signUpRequest.password = signupRequestDTO.password;
//            signUpRequest.fullName = signupRequestDTO.fullName;
//            signUpRequest.role = signupRequestDTO.role;
//
//            if (signUpRequest == null) {
//                return ResponseEntity.badRequest().body("Registration session expired. Please sign up again.");
//            }
//            if (!otp.equals(signupRequestDTO.getOtp())) {
//                return ResponseEntity.badRequest().body("Invalid OTP. Please try again.");
//            }
//
//            // Tạo tài khoản người dùng mới
//            User user = new User(signUpRequest.getUserName(),
//                    signUpRequest.getEmail(),
//                    encoder.encode(signUpRequest.getPassword()),
//                    signUpRequest.getFullName());
//
//            Set<String> strRoles = signUpRequest.getRole();
//            Set<Role> roles = new HashSet<>();
//
//            if (strRoles == null) {
//                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                roles.add(userRole);
//            } else {
//                strRoles.forEach(role -> {
//                    switch (role) {
//                        case "admin":
//                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(adminRole);
//                            break;
//                        case "shipper":
//                            Role shipperRole = roleRepository.findByName(ERole.ROLE_SHIPPER)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(shipperRole);
//                            break;
//                        default:
//                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(userRole);
//                    }
//                });
//            }
//
//            user.setRoles(roles);
//            userRepository.save(user);
//
//            // Xóa thông tin tạm thời
//            redisTemplate.delete(email);
//
//            return ResponseEntity.ok("User registered successfully!");
//        }catch(Exception e){
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Failed when validate OTP !");
//        }
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpNewAccount(@RequestBody SignupRequestDTO signUpRequest){
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }
        try {
        User user = new User(signUpRequest.getUserName(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getFullName());

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                            break;
                        case "shipper":
                            Role shipperRole = roleRepository.findByName(ERole.ROLE_SHIPPER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(shipperRole);
                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);



            return ResponseEntity.ok("User registered successfully!");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed when validate OTP !");
        }
    }

}
