package org.fai.study.projectsem4.entity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInforDTO {
    private Integer userId;
    private String userName;
    private String fullName;
    private String email;
    private String roleName;
    
}
