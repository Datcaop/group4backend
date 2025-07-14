package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.ENUM.ERole;
import org.fai.study.projectsem4.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
}
