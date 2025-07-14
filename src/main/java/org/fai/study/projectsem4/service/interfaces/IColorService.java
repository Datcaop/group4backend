package org.fai.study.projectsem4.service.interfaces;

import org.fai.study.projectsem4.entity.DTOs.ColorDTO;
import org.fai.study.projectsem4.entity.DTOs.GenderDTO;

import java.util.List;

public interface IColorService {
    List<ColorDTO> getAllColor();
}
