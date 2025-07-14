package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.entity.Image;
import org.fai.study.projectsem4.entity.Product;
import org.fai.study.projectsem4.repository.ImageRepo;
import org.fai.study.projectsem4.repository.ProductRepo;
import org.fai.study.projectsem4.service.interfaces.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class ImageUploadController {
//    @Autowired
//    private BlogStorageService blogStorageService;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private IS3Service is3Service;

//    @PostMapping("/image/uploadImage")
//    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
//        try{
//            String fileUrl = blogStorageService.uploadFile(file);
//
//            Image image = new Image();
//            image.setUrl(fileUrl);
//            imageRepo.save(image);
//            return ResponseEntity.ok().body("Image has been uploaded!");
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
//        }
//    }

    @PostMapping("/image/S3UploadImage")
    public ResponseEntity<?> uploadImageToS3(@RequestParam("file") MultipartFile file){
        try {
            String fileUrl = is3Service.uploadFile(file);
            return ResponseEntity.ok().body(fileUrl);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
        }
    }
}
