package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query(value = "select p.product_id, p.product_name ,p.price,p.sold_number, " +
            "(select i.url from images i where i.product_id = p.product_id ORDER BY i.image_id ASC LIMIT 1) as image_url " +
            "from products p " +
            "join genders g on p.gender_id = g.gender_id " +
            "join categories c on p.cate_id = c.cate_id " +
            "join colors co on p.color_id = co.color_id " +
            "left join product_type pt on p.product_type_id = pt.product_type_id " +
            "where c.cate_id= :cateId",nativeQuery = true)
    List<Object[]> findAllProductByCateId(@Param("cateId") Integer cateId);

    @Query(value = "select p.product_id,p.code,p.product_name,p.price,p.sold_number,i.url,p.weight,g.gender_name,p.brand,co.color_name,c.cate_description " +
            "from products p " +
            "join genders g on p.gender_id = g.gender_id " +
            "join categories c on p.cate_id = c.cate_id " +
            "join colors co on p.color_id = co.color_id " +
            "join images i on p.product_id = i.product_id " +
            "where p.product_id= :productId order by i.image_id ASC",nativeQuery = true)
    List<Object[]> findProductByProductId(@Param("productId") Integer productId);


    @Query(value = "WITH RankedImages AS ( " +
            "SELECT " +
            "i.product_id, " +
            "i.url, " +
            "ROW_NUMBER() OVER (PARTITION BY i.product_id ORDER BY i.image_id ASC) AS rn " +
            "FROM images i " +
            ") " +
            "SELECT " +
            "p.product_id, " +
            "p.product_name, " +
            "p.price, " +
            "p.sold_number, " +
            "MAX(g.gender_name) AS gender_name, " +
            "MAX(c.cate_nam) AS cate_name, " +
            "MAX(co.color_name) AS color_name, " +
            "MAX(CASE WHEN ri.rn = 1 THEN ri.url END) AS image_main, " +
            "MAX(CASE WHEN ri.rn = 2 THEN ri.url END) AS image_1, " +
            "MAX(CASE WHEN ri.rn = 3 THEN ri.url END) AS image_2 " +
            "FROM products p " +
            "LEFT JOIN RankedImages ri ON p.product_id = ri.product_id " +
            "JOIN genders g ON p.gender_id = g.gender_id " +
            "JOIN categories c ON p.cate_id = c.cate_id " +
            "JOIN colors co ON p.color_id = co.color_id " +
            "LEFT JOIN product_type pt ON p.product_type_id = pt.product_type_id " +
            "GROUP BY " +
            "p.product_id, " +
            "p.product_name, " +
            "p.price, " +
            "p.sold_number",nativeQuery = true)
            List<Object[]> adminGetAllProduct();

    @Query(value = "select p.product_id,p.product_name,p.price,p.sold_number,(select i.url from images i where i.product_id = p.product_id ORDER BY i.image_id ASC LIMIT 1) as image_url  from products p order by sold_number DESC LIMIT 15 ",nativeQuery = true)
        List<Object[]> getTrendingProducts();

    @Query(value = "SELECT " +
            "p.product_id, " +
            "p.product_name, " +
            "p.price, " +
            "MIN(i.url) AS image_url " +
            "FROM " +
            "products p " +
            "LEFT JOIN " +
            "images i " +
            "ON " +
            "p.product_id = i.product_id " +
            "WHERE " +
            "LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "GROUP BY " +
            "p.product_id, p.product_name, p.price",
            nativeQuery = true)
    List<Object[]> searchProductWithImage(@Param("keyword") String keyword);



}
