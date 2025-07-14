package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.Category;
import org.fai.study.projectsem4.entity.DTOs.CategoryHeaderDTO;
import org.fai.study.projectsem4.entity.DTOs.GenderDTO;
import org.fai.study.projectsem4.entity.Gender;
import org.fai.study.projectsem4.repository.GenderRepo;
import org.fai.study.projectsem4.service.interfaces.IGenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenderService implements IGenderService {
    @Autowired
    GenderRepo genderRepo;

    @Override
    public List<GenderDTO> getAllGender() {
        try {
            List<Gender> listGender= genderRepo.findAll();
            List<GenderDTO> genderDTOList=new ArrayList<>();
            for(Gender gender:listGender){
                GenderDTO genderDTO =new GenderDTO();
                genderDTO.setGenderId(gender.getGenderId());
                genderDTO.setGenderName(gender.getGenderName());
                genderDTOList.add(genderDTO);
            }
            return genderDTOList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
