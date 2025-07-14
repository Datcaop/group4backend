package org.fai.study.projectsem4.controller;

import org.fai.study.projectsem4.entity.ENUM.ERole;
import org.fai.study.projectsem4.entity.Role;
import org.fai.study.projectsem4.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RoleController {
    @Autowired
    private RoleRepo roleRepository;

    @PostMapping("/role")
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        if (roleRepository.existsByName(role.getName())) {
            return ResponseEntity.badRequest().body("Role already exists!");
        }
        Role newRole = roleRepository.save(role);
        return ResponseEntity.ok(newRole);
    }
}
