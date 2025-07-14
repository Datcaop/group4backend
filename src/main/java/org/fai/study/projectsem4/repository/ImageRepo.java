package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepo extends JpaRepository<Image, Integer> {
    Optional<Image> findFirstByProduct_ProductId(Integer productId);

}
