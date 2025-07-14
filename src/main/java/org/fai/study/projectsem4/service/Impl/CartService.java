package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.Cart;
import org.fai.study.projectsem4.entity.DTOs.CartDTO;
import org.fai.study.projectsem4.entity.DTOs.CartDetailDTO;
import org.fai.study.projectsem4.entity.DTOs.CartResultDTO;
import org.fai.study.projectsem4.entity.Product;
import org.fai.study.projectsem4.entity.User;
import org.fai.study.projectsem4.repository.CartRepo;
import org.fai.study.projectsem4.repository.ProductRepo;
import org.fai.study.projectsem4.repository.UserRepo;
import org.fai.study.projectsem4.service.interfaces.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class CartService implements ICartService {
    @Autowired
    CartRepo cartRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserRepo userRepo;


    @Override
    public CartResultDTO addCart(CartDTO cartDTO) {
        try {

            Product product = productRepo.findById(cartDTO.getProduct_id())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + cartDTO.getProduct_id()));

            User user = userRepo.findById(cartDTO.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + cartDTO.getUser_id()));
            Cart existingCart = cartRepo.getCartByProductId(cartDTO.getProduct_id(), cartDTO.getUser_id());
            if (existingCart != null) {
                    return new CartResultDTO("The product has been added to the cart, please check the cart !",500);
            }

            // Tạo đối tượng Cart
            Cart cart = new Cart();
            cart.setSize(cartDTO.getSize());
            cart.setQuantity(cartDTO.getQuantity());
            cart.setProduct(product);
            cart.setUser(user);

            // Lưu Cart vào database
            Cart savedCart = cartRepo.save(cart);

            // Chuyển Cart sang DTO để trả về
            CartDTO savedCartDTO = new CartDTO();
            savedCartDTO.setSize(savedCart.getSize());
            savedCartDTO.setQuantity(savedCart.getQuantity());
            savedCartDTO.setProduct_id(savedCart.getProduct().getProductId());
            savedCartDTO.setUser_id(savedCart.getUser().getUserId());

            return new CartResultDTO("Add product to cart successfully!",200);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String deleteCart(Integer cartId) {
        try {
            cartRepo.deleteById(cartId);
            return "Delete cart successfully!";
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "Delete Cart Failed !";
        }

    }

    @Override
    public List<CartDetailDTO> getCartByUserId(Integer userId) {
        try {
            List<Object[]> AllCartFromDb =  cartRepo.getAllCartByUserId(userId);
            List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();
            for (Object[] data : AllCartFromDb) {
                CartDetailDTO cartDetailDTO = new CartDetailDTO();
                cartDetailDTO.setCartId((Integer) data[0]);
                cartDetailDTO.setProductId((Integer) data[1]);
                cartDetailDTO.setUrl((String) data[2]);
                cartDetailDTO.setName((String) data[3]);
                cartDetailDTO.setCode((String) data[4]);
                cartDetailDTO.setSize((Integer) data[5]);
                cartDetailDTO.setQuantity((Integer) data[6]);
                cartDetailDTO.setPrice((Double) data[7]);

                cartDetailDTOList.add(cartDetailDTO);
            }
            return cartDetailDTOList;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }


    }

}

