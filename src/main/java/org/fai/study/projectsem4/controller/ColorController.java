package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.entity.Color;
import org.fai.study.projectsem4.repository.ColorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ColorController {
    @Autowired
    private ColorRepo colorRepo;
    @PostMapping("/color")
    public ResponseEntity<?> addNewColor(@RequestBody Color color){
        if (color.getColorName() == null || color.getColorName().isEmpty()) {
            return ResponseEntity.badRequest().body("Color name cannot be empty");
        }
        Color savedColor = colorRepo.save(color);
        return ResponseEntity.ok(savedColor);
    }
}
