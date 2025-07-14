//package org.fai.study.projectsem4.service.Impl;
//
//import com.azure.storage.blob.BlobClient;
//import com.azure.storage.blob.BlobContainerClient;
//import com.azure.storage.blob.BlobServiceClient;
//import com.azure.storage.blob.BlobServiceClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//public class BlogStorageService {
//    @Value("${azure.storage.account-name}")
//    private String accountName;
//    @Value("${azure.storage.account-key}")
//    private String accountKey;
//    @Value("${azure.storage.container-name}")
//    private String containerName;
//
//    public BlobServiceClient getBlobServiceClient() {

//        return new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
//    }
//
//    public String uploadFile(MultipartFile file) throws IOException {
//            String filename = UUID.randomUUID()+ "-" +file.getOriginalFilename();
////            lấy ra container
//        BlobContainerClient containerClient = getBlobServiceClient().getBlobContainerClient(containerName);
////        upload file lên container
//        BlobClient blobClient = containerClient.getBlobClient(filename);
//        blobClient.upload(file.getInputStream(),file.getSize(),true);
//
//        return blobClient.getBlobUrl();
//    }
//
//    public String uploadAvatarFile(MultipartFile file,Integer shipperId) throws IOException {
//        String filename = UUID.randomUUID()+ "-AvatarImageOfShipper" +shipperId+ file.getOriginalFilename();
////            lấy ra container
//        BlobContainerClient containerClient = getBlobServiceClient().getBlobContainerClient(containerName);
////        upload file lên container
//        BlobClient blobClient = containerClient.getBlobClient(filename);
//        blobClient.upload(file.getInputStream(),file.getSize(),true);
//
//        return blobClient.getBlobUrl();
//    }
//}
