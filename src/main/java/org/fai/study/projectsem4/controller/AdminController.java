package org.fai.study.projectsem4.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fai.study.projectsem4.entity.DTOs.*;
import org.fai.study.projectsem4.entity.Gender;
import org.fai.study.projectsem4.entity.Image;
import org.fai.study.projectsem4.entity.Product;
import org.fai.study.projectsem4.entity.User;
import org.fai.study.projectsem4.repository.*;
import org.fai.study.projectsem4.service.Impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")

public class AdminController {
    @Autowired
    OrderService orderService;

    @Autowired
    ShipperService shipperService;

    @Autowired
    ShippingService shippingService;

    @Autowired
    ProductService productService;

//    @Autowired
//    BlogStorageService blogStorageService;
    @Autowired
    S3Service s3Service;

    @Autowired
    ImageRepo imageRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private GenderService genderService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserServiceImpl userService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "This is the admin dashboard. Only admins can see this.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderResponseDTO>> adminOrders() {
        List<AdminOrderResponseDTO> allOrder = orderService.getAllOrderAdmin();
        if (allOrder != null){
            return ResponseEntity.ok(allOrder);
        }else {
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orderDetailInfor")
    public ResponseEntity<AdminOrderDetailResponseDTO> adminOrderDetail(@RequestParam Integer orderId) {
        AdminOrderDetailResponseDTO adminOrderDetailResponseDTO = orderService.getOrderDetailInforAdminByOrderId(orderId);
        if (adminOrderDetailResponseDTO != null){
            return ResponseEntity.ok(adminOrderDetailResponseDTO);
        }else {
            return ResponseEntity.noContent().build();
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllShipperInforWithStatus")
    public ResponseEntity<List<ShipperInforWithStatusDTO>> getShipperInfor(){
        List<ShipperInforWithStatusDTO> shipperInfor = shipperService.getAllShipperInfo();
        if (shipperInfor != null){
            return ResponseEntity.ok(shipperInfor);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/transferOrderToShipper")
    public ResponseEntity<ShippingResponseDTO> transferOrderToShipper(@RequestBody ShippingRequestDTO shippingRequestDTO){
        ShippingResponseDTO responseDTO = shippingService.addShipping(shippingRequestDTO);
        if (responseDTO != null){
            return ResponseEntity.ok(responseDTO);
        }
        return ResponseEntity.noContent().build();
    }

//    api về sản phẩm
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllProductAdmin")
    public ResponseEntity<List<ProductDTO>> getAllProductAdmin(){
        List<ProductDTO> productDTOList = productService.getAllProductsAdmin();
        if (productDTOList != null){
            return ResponseEntity.ok(productDTOList);
        }return ResponseEntity.noContent().build();
    }

    // Tạm thời comment lại ,dùng controller khi lưu ảnh bằng Azure
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/createNewProduct")
//    public ResponseEntity<?> createNewProduct(
//            @RequestParam("newProductDTO") String newProductDTOString,
//            @RequestParam("mainImage") MultipartFile mainImage,
//            @RequestParam("secondImage") MultipartFile secondImage,
//            @RequestParam("thirdImage") MultipartFile thirdImage) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            NewProductDTO newProductDTO = objectMapper.readValue(newProductDTOString, NewProductDTO.class);
//            Integer productId = productService.addANewProduct(newProductDTO);
//
//            if (productId != null) {
//                Product product = productRepo.findById(productId)
//                        .orElseThrow(() -> new RuntimeException("Product not found"));
//
//                // Upload và lưu ảnh chính
//                String fileUrl1 = blogStorageService.uploadFile(mainImage);
//                Image mainImage1 = new Image();
//                mainImage1.setUrl(fileUrl1);
//                mainImage1.setProduct(product);
//                imageRepo.save(mainImage1);
//
//                // Upload và lưu ảnh phụ (secondImage)
//                String fileUrl2 = blogStorageService.uploadFile(secondImage);
//                Image secondImage2 = new Image();
//                secondImage2.setUrl(fileUrl2);
//                secondImage2.setProduct(product);
//                imageRepo.save(secondImage2);
//
//                // Upload và lưu ảnh phụ (thirdImage)
//                String fileUrl3 = blogStorageService.uploadFile(thirdImage);
//                Image thirdImage3 = new Image();
//                thirdImage3.setUrl(fileUrl3);
//                thirdImage3.setProduct(product);
//                imageRepo.save(thirdImage3);
//
//                return ResponseEntity.ok("Add a new product successful!");
//            }
//
//            return ResponseEntity.noContent().build();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to upload images. Please try again.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred while creating a new product.");
//        }
//        }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createNewProduct")
    public ResponseEntity<?> createNewProduct(
            @RequestParam("newProductDTO") String newProductDTOString,
            @RequestParam("mainImage") MultipartFile mainImage,
            @RequestParam("secondImage") MultipartFile secondImage,
            @RequestParam("thirdImage") MultipartFile thirdImage) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NewProductDTO newProductDTO = objectMapper.readValue(newProductDTOString, NewProductDTO.class);

            Integer productId = productService.addANewProduct(newProductDTO);
            if (productId != null) {
                Product product = productRepo.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                // Upload và lưu ảnh chính
                String fileUrl1 = s3Service.uploadFile(mainImage);
                Image mainImage1 = new Image();
                mainImage1.setUrl(fileUrl1);
                mainImage1.setProduct(product);
                imageRepo.save(mainImage1);

                // Upload và lưu ảnh phụ (secondImage)
                String fileUrl2 = s3Service.uploadFile(secondImage);
                Image secondImage2 = new Image();
                secondImage2.setUrl(fileUrl2);
                secondImage2.setProduct(product);
                imageRepo.save(secondImage2);

                // Upload và lưu ảnh phụ (thirdImage)
                String fileUrl3 = s3Service.uploadFile(thirdImage);
                Image thirdImage3 = new Image();
                thirdImage3.setUrl(fileUrl3);
                thirdImage3.setProduct(product);
                imageRepo.save(thirdImage3);

                return ResponseEntity.ok("Add a new product successful!");
            }

            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload images. Please try again.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating a new product.");
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllCategory")
    List<CategoryHeaderDTO> getAllCategoryHeader() {
        return categoryService.getAllCateHeader();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllGender")
    List<GenderDTO> getAllGender() {
        return genderService.getAllGender();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllColor")
    List<ColorDTO> getAllColor() {
        return colorService.getAllColor();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteAProduct")
    ResponseEntity<?> deleteAProduct(@RequestParam Integer productId ){
        Product product = productRepo.findById(productId).orElseThrow(()-> new RuntimeException("Product not found"));
        try{
            productRepo.deleteById(productId);
            return ResponseEntity.ok("Product deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().body("Product deleted failed!");
        }
    }

//    api shipper
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteAShipper")
    ResponseEntity<?> deleteAShipper(@RequestParam Integer shipperId ){
        User user= userRepo.findById(shipperId).orElseThrow(()-> new RuntimeException("Shipper not found"));
        try{
            userRepo.deleteById(shipperId);
            return ResponseEntity.ok("Shipper deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().body("Shipper deleted failed!");
        }
    }


//    api quản lí user
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userManage")
    ResponseEntity<List<UserDTO>> getALLUserInfor(){
        List<UserDTO> userDTOList = userService.getAllInforUserWithRole();
        if (userDTOList != null){
            return ResponseEntity.ok(userDTOList);
        }return ResponseEntity.noContent().build();

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteAUser")
    ResponseEntity<?> deleteUser(@RequestParam Integer userId ){
        User user= userRepo.findById(userId).orElseThrow(()-> new RuntimeException("User not found! "));
        try{
            userRepo.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().body("User deleted failed!");
        }
    }

//    api thống kê
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthRevenueDashboard")
    public ResponseEntity<List<MonthRevenueDashboardDTO>> getMonthRevenueDashboard(){
        List<MonthRevenueDashboardDTO> monthRevenueDashboardDTOList = orderService.getMonthRevenueDashboard();
        if (monthRevenueDashboardDTOList != null){
            return ResponseEntity.ok(monthRevenueDashboardDTOList);
        }return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statusOrderDashboard")
    public ResponseEntity<List<StatusOrderDashboardDTO>> getStatusOrderDashboard(){
        List<StatusOrderDashboardDTO> statusOrderDashboardDTOList = orderService.getStatusOrderDashboard();
        if (statusOrderDashboardDTOList != null){
            return ResponseEntity.ok(statusOrderDashboardDTOList);
        }return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userRoleDashboard")
    public ResponseEntity<List<UserRoleDTO>> getUserRoleDashboard(){
        List<UserRoleDTO> userRoleDTOList = orderService.getUserRoleDashboard();
        if (userRoleDTOList != null){
            return ResponseEntity.ok(userRoleDTOList);
        }return ResponseEntity.noContent().build();
    }


}
