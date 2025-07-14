package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepo extends JpaRepository<Gender, Integer> {
}
