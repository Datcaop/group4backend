package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.entity.Category;
import org.fai.study.projectsem4.entity.DTOs.CategoryHeaderDTO;
import org.fai.study.projectsem4.repository.CategoryRepo;
import org.fai.study.projectsem4.service.interfaces.ICateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class CategoryController {
    @Autowired
    ICateService cateService;
    @Autowired
    CategoryRepo categoryRepo;
    @GetMapping("/getHeader")
    List<CategoryHeaderDTO> getAllCategoryHeader() {
        return cateService.getAllCateHeader();
    }
    @PostMapping("/addCategory")
    ResponseEntity<?> addNewCategory(@RequestBody Category category){
        Category savedCategory = categoryRepo.save(category);
        return ResponseEntity.ok(savedCategory);
    }
}
