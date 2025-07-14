package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.Category;
import org.fai.study.projectsem4.entity.Color;
import org.fai.study.projectsem4.entity.DTOs.*;
import org.fai.study.projectsem4.entity.Gender;
import org.fai.study.projectsem4.entity.Product;
import org.fai.study.projectsem4.repository.CategoryRepo;
import org.fai.study.projectsem4.repository.ColorRepo;
import org.fai.study.projectsem4.repository.GenderRepo;
import org.fai.study.projectsem4.repository.ProductRepo;
import org.fai.study.projectsem4.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ProductService implements IProductService {
    @Autowired
    ProductRepo productRepo;

    @Autowired
    GenderRepo genderRepo;

    @Autowired
    ColorRepo colorRepo;

    @Autowired
    CategoryRepo categoryRepo;

//    @Autowired
//    BlogStorageService blogStorageService;
    @Override
    public List<ProductHomeDTO> getAllProductsByCategoryId(Integer categoryId) {
        List<ProductHomeDTO> productHomeDTOList = new ArrayList<>();
        List<Object[]> DataGetFromDB = productRepo.findAllProductByCateId(categoryId);
        for (Object[] data : DataGetFromDB) {
            ProductHomeDTO productHomeDTO = new ProductHomeDTO();
            productHomeDTO.setProductId((Integer) data[0]);
            productHomeDTO.setProductName((String) data[1]);
            productHomeDTO.setPrice((Double) data[2]);
            productHomeDTO.setSoldNumber((Integer) data[3]);
            productHomeDTO.setImage_url((String) data[4]);
            productHomeDTOList.add(productHomeDTO);
        }
        return productHomeDTOList;
    }

    @Override
    public ProductDetailDTO findProductDetailById(Integer productId) {
        List<Object[]> DataGetFromDB = productRepo.findProductByProductId(productId);
        Object[] firstRow = DataGetFromDB.get(0);

        ProductDetailDTO productHomeDTO = new ProductDetailDTO();
        productHomeDTO.setProductId((Integer) firstRow[0]);
        productHomeDTO.setCode((String) firstRow[1]);
        productHomeDTO.setName((String) firstRow[2]);
        productHomeDTO.setPrice((Double) firstRow[3]);
        productHomeDTO.setSold((Integer) firstRow[4]);
        for (int i = 0; i < Math.min(DataGetFromDB.size(), 3); i++) {
            Object[] row = DataGetFromDB.get(i);
            String url = (String) row[5];

            switch (i) {
                case 0 -> productHomeDTO.setMainImage(url);
                case 1 -> productHomeDTO.setImage2(url);
                case 2 -> productHomeDTO.setImage3(url);
            }
        }
        DescriptionProductDTO description = new DescriptionProductDTO();
        description.setWeight((Double) firstRow[6]);
        description.setGender((String) firstRow[7]);
        description.setBrand((String) firstRow[8]);
        description.setColor((String) firstRow[9]);
        description.setDescription((String) firstRow[10]);

        productHomeDTO.setDescription(description);

        return productHomeDTO;
    }

    @Override
    public Integer addANewProduct(NewProductDTO newProductDTO) {
        if (!genderRepo.existsById(newProductDTO.getGenderId()) || !colorRepo.existsById(newProductDTO.getColorId()) || !categoryRepo.existsById(newProductDTO.getCategoryId())){
            return null;
        }

        Gender gender = genderRepo.findById(newProductDTO.getGenderId())
                .orElseThrow(() -> new RuntimeException("Gender not found"));
        Color color = colorRepo.findById(newProductDTO.getColorId())
                .orElseThrow(() -> new RuntimeException("Color not found"));
        Category category = categoryRepo.findById(newProductDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setProductName(newProductDTO.getProductName());
        product.setPrice(newProductDTO.getPrice());
        product.setSold(10);
        product.setCode(newProductDTO.getCode());
        product.setBrand(newProductDTO.getBrand());
        product.setWeight(newProductDTO.getWeight());
        product.setGender(gender);
        product.setColor(color);
        product.setCategory(category);

         Product savedProduct = productRepo.save(product);

        return savedProduct.getProductId();
    }

    @Override
    public List<ProductDTO> getAllProductsAdmin() {
        List<Object[]> dataFromDB=  productRepo.adminGetAllProduct();
        List<ProductDTO> productHomeDTOList = new ArrayList<>();
        for (Object[] data : dataFromDB) {
            ProductDTO productHomeDTO = new ProductDTO();
            productHomeDTO.setProductId((Integer) data[0]);
            productHomeDTO.setProductName((String) data[1]);
            productHomeDTO.setPrice((Double) data[2]);
            productHomeDTO.setSoldNumber((Integer) data[3]);
            productHomeDTO.setGenderName((String) data[4]);
            productHomeDTO.setCateName((String) data[5]);
            productHomeDTO.setColorName((String) data[6]);
            productHomeDTO.setImageMain((String) data[7]);
            productHomeDTO.setImageSecond((String) data[8]);
            productHomeDTO.setImageThird((String) data[9]);

            productHomeDTOList.add(productHomeDTO);
        }
        return productHomeDTOList;
    }

    @Override
    public List<ProductHomeDTO> getTrendingProducts() {
        List<ProductHomeDTO> productHomeDTOList = new ArrayList<>();
        List<Object[]> DataGetFromDB = productRepo.getTrendingProducts();
        for (Object[] data : DataGetFromDB) {
            ProductHomeDTO productHomeDTO = new ProductHomeDTO();
            productHomeDTO.setProductId((Integer) data[0]);
            productHomeDTO.setProductName((String) data[1]);
            productHomeDTO.setPrice((Double) data[2]);
            productHomeDTO.setSoldNumber((Integer) data[3]);
            productHomeDTO.setImage_url((String) data[4]);
            productHomeDTOList.add(productHomeDTO);
        }
        return productHomeDTOList;
    }

    @Override
    public List<ProductSearchDTO> getProductsBySearch(String keyword) {
        List<Object[]> dataFromDB = productRepo.searchProductWithImage(keyword);
        List<ProductSearchDTO> productSearchDTOList = new ArrayList<>();
        for (Object[] data : dataFromDB) {
            ProductSearchDTO productSearchDTO = new ProductSearchDTO();
            productSearchDTO.setProductId((Integer) data[0]);
            productSearchDTO.setProductName((String) data[1]);
            productSearchDTO.setPrice((Double) data[2]);
            productSearchDTO.setImage((String) data[3]);
            productSearchDTOList.add(productSearchDTO);
        }
        return productSearchDTOList;
    }


}
