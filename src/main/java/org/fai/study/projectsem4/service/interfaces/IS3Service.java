package org.fai.study.projectsem4.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IS3Service {
    public String uploadFile(MultipartFile file)throws IOException;
}
