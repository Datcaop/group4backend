package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
    @Query(value = "select * from carts where product_id= :productId and user_id= :userId",nativeQuery = true)
    Cart getCartByProductId(Integer productId, Integer userId);

    @Query(value = "SELECT " +
            "c.cart_id, " +
            "p.product_id, " +
            "(SELECT i.url FROM images i WHERE i.product_id = c.product_id LIMIT 1) AS url, " +
            "p.product_name, " +
            "p.code, " +
            "c.size, " +
            "c.quantity, " +
            "p.price " +
            "FROM " +
            "carts c " +
            "JOIN " +
            "products p ON c.product_id = p.product_id " +
            "WHERE " +
            "c.user_id = :userId", nativeQuery = true)
    List<Object[]> getAllCartByUserId(Integer userId);

}
