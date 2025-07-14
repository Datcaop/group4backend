package org.fai.study.projectsem4.service.interfaces;

import org.fai.study.projectsem4.entity.DTOs.CartDTO;
import org.fai.study.projectsem4.entity.DTOs.CartDetailDTO;
import org.fai.study.projectsem4.entity.DTOs.CartResultDTO;

import java.util.List;

public interface ICartService {
    CartResultDTO addCart(CartDTO cartDTO);

    String deleteCart(Integer cartId);

    List<CartDetailDTO> getCartByUserId(Integer userId);

}
