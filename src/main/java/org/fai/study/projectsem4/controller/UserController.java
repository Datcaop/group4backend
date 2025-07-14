package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.configuration.JWTUtils;
import org.fai.study.projectsem4.entity.DTOs.AdminOrderResponseDTO;
import org.fai.study.projectsem4.entity.DTOs.UserInforDTO;
import org.fai.study.projectsem4.service.interfaces.IOrderService;
import org.fai.study.projectsem4.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    IUserService userService;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    IOrderService orderService;
    @GetMapping("/profile")
    public UserInforDTO userProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserIdFromJwtToken(token);
        return userService.getUserInfor(userId);
    }

    @GetMapping("/userOrders")
    public ResponseEntity<List<AdminOrderResponseDTO>> userOrders(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserIdFromJwtToken(token);

        List<AdminOrderResponseDTO> allOrder = orderService.getAllOrderUser(userId);
        if (allOrder != null){
            return ResponseEntity.ok(allOrder);
        }else {
            return ResponseEntity.noContent().build();
        }
    }



}
