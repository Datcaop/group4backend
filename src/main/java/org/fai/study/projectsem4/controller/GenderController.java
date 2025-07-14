package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.entity.Color;
import org.fai.study.projectsem4.entity.Gender;
import org.fai.study.projectsem4.repository.GenderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class GenderController {
    @Autowired
    GenderRepo genderRepo;
    @PostMapping("/gender")
    public ResponseEntity<?> addNewColor(@RequestBody Gender gender){
        if (gender.getGenderName() == null || gender.getGenderName().isEmpty()) {
            return ResponseEntity.badRequest().body("Gender name cannot be empty");
        }
        Gender savedGender = genderRepo.save(gender);
        return ResponseEntity.ok(savedGender);
    }
}
