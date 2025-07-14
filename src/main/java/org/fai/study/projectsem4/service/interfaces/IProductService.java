package org.fai.study.projectsem4.service.interfaces;


import org.fai.study.projectsem4.entity.DTOs.*;
import org.fai.study.projectsem4.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IProductService {
    List<ProductHomeDTO> getAllProductsByCategoryId(Integer categoryId);
    ProductDetailDTO findProductDetailById(Integer productId);
    Integer addANewProduct(NewProductDTO newProductDTO);
    List<ProductDTO> getAllProductsAdmin();
    List<ProductHomeDTO> getTrendingProducts();
    List<ProductSearchDTO> getProductsBySearch(String keyword);

}
