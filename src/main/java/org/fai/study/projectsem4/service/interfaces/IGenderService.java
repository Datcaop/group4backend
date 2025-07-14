package org.fai.study.projectsem4.service.interfaces;

import org.fai.study.projectsem4.entity.DTOs.CategoryHeaderDTO;
import org.fai.study.projectsem4.entity.DTOs.GenderDTO;

import java.util.List;

public interface IGenderService {
    List<GenderDTO> getAllGender();
}
