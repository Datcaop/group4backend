package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.Color;
import org.fai.study.projectsem4.entity.DTOs.ColorDTO;
import org.fai.study.projectsem4.entity.DTOs.GenderDTO;
import org.fai.study.projectsem4.entity.Gender;
import org.fai.study.projectsem4.repository.ColorRepo;
import org.fai.study.projectsem4.repository.GenderRepo;
import org.fai.study.projectsem4.service.interfaces.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColorService implements IColorService {
    @Autowired
    ColorRepo colorRepo;

    @Override
    public List<ColorDTO> getAllColor() {
        try {
            List<Color> listColor= colorRepo.findAll();
            List<ColorDTO> colorDTOList=new ArrayList<>();
            for(Color color:listColor){
                ColorDTO colorDTO =new ColorDTO();
                colorDTO.setColorId(color.getColorId());
                colorDTO.setColorName(color.getColorName());
                colorDTOList.add(colorDTO);
            }
            return colorDTOList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
