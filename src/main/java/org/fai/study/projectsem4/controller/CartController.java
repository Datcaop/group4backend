package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.configuration.JWTUtils;
import org.fai.study.projectsem4.entity.DTOs.CartDTO;
import org.fai.study.projectsem4.entity.DTOs.CartDetailDTO;
import org.fai.study.projectsem4.entity.DTOs.CartGetFromFe;
import org.fai.study.projectsem4.entity.DTOs.CartResultDTO;
import org.fai.study.projectsem4.service.Impl.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    JWTUtils jwtUtils;
    @PostMapping("/addCart")
    public CartResultDTO addToCart(@RequestBody CartGetFromFe cartDTOFromFe, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserIdFromJwtToken(token);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setQuantity(cartDTOFromFe.getQuantity());
        cartDTO.setSize(cartDTOFromFe.getSize());
        cartDTO.setProduct_id(cartDTOFromFe.getProduct_id());
        cartDTO.setUser_id(userId);
        CartResultDTO savedCart = cartService.addCart(cartDTO);
        return savedCart;
    }

    @GetMapping("/getCart")
    public List<CartDetailDTO> getAllCartByUserId(@RequestHeader("Authorization")String authHeader){
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserIdFromJwtToken(token);
        return cartService.getCartByUserId(userId);
    }

    @DeleteMapping("/deleteCart")
    public String deleteCart(@RequestParam Integer cartId) {
        return cartService.deleteCart(cartId);
    }
}
