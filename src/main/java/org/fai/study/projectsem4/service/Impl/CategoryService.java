package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.Category;
import org.fai.study.projectsem4.entity.DTOs.CategoryHeaderDTO;
import org.fai.study.projectsem4.repository.CategoryRepo;
import org.fai.study.projectsem4.service.interfaces.ICartService;
import org.fai.study.projectsem4.service.interfaces.ICateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICateService {
    @Autowired
    CategoryRepo categoryRepo;


    @Override
    public List<CategoryHeaderDTO> getAllCateHeader() {
        try {
            List<Category> listCategory= categoryRepo.findAll();
            List<CategoryHeaderDTO> categoryHeaderDTOList=new ArrayList<>();

            for(Category category:listCategory){
                CategoryHeaderDTO categoryHeaderDTO=new CategoryHeaderDTO();
                categoryHeaderDTO.setId(category.getCateId());
                categoryHeaderDTO.setName(category.getCateName());
                categoryHeaderDTOList.add(categoryHeaderDTO);
            }
            return categoryHeaderDTOList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
