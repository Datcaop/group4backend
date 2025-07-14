package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.entity.DTOs.ProductDetailDTO;
import org.fai.study.projectsem4.entity.DTOs.ProductHomeDTO;
import org.fai.study.projectsem4.entity.DTOs.ProductSearchDTO;
import org.fai.study.projectsem4.service.Impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/productHome")
    public List<ProductHomeDTO> getAllProductByCategoryId(@RequestParam Integer categoryId) {
        return productService.getAllProductsByCategoryId(categoryId);
    }

    @GetMapping("/productDetail")
    public ProductDetailDTO getProductDetailByProductId(@RequestParam Integer productId) {
        return productService.findProductDetailById(productId);
    }
    @GetMapping("/trending")
    public List<ProductHomeDTO> getTrendingProducts() {
        return productService.getTrendingProducts();
    }
    @GetMapping("/search")
    public List<ProductSearchDTO> getProductsBySearch(@RequestParam String keyword) {
        return productService.getProductsBySearch(keyword);
    }
}
