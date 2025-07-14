package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.configuration.JWTUtils;
import org.fai.study.projectsem4.entity.DTOs.OrderDTO;
import org.fai.study.projectsem4.service.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    IOrderService orderService;
    @Autowired
    JWTUtils jwtUtils;
    @PostMapping("/orderCOD")
    public String addOrder(@RequestBody OrderDTO orderDTO,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserIdFromJwtToken(token);
        return orderService.addNewOrder(orderDTO,userId);
    }
}
