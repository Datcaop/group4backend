package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.DTOs.ShipperInForDTO;
import org.fai.study.projectsem4.entity.DTOs.UserDTO;
import org.fai.study.projectsem4.entity.DTOs.UserInforDTO;
import org.fai.study.projectsem4.entity.User;
import org.fai.study.projectsem4.repository.UserRepo;
import org.fai.study.projectsem4.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserRepo userRepo;
    @Override
    public UserInforDTO getUserInfor(Integer userId) {


        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User Id Not Found :" +userId));
        UserInforDTO userInforDTO = new UserInforDTO();
        userInforDTO.setUserId(user.getUserId());
        userInforDTO.setUserName(user.getUserName());
        userInforDTO.setFullName(user.getFullName());
        userInforDTO.setEmail(user.getEmail());

        String roleNames = user.getRoles().stream()
                .map(role -> role.getName().name()) // Lấy tên từ Enum ERole
                .collect(Collectors.joining(", "));
        userInforDTO.setRoleName(roleNames);
        return userInforDTO;
    }

    @Override
    public List<UserDTO> getAllInforUserWithRole() {
        List<Object[]> userListFromDB = userRepo.getAllUserInforWithRoles();
        List<UserDTO> userDTOList= new ArrayList<>();

        for(Object[] data : userListFromDB){
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId((Integer) data[0]);
            userDTO.setAddress((String) data[1]);
            userDTO.setEmail((String) data[2]);
            userDTO.setPhoneNumber((String) data[3]);
            userDTO.setFullName((String) data[4]);
            userDTO.setAvatar("https://group4blog.blob.core.windows.net/avatar/avtar.jpg");
            userDTO.setRoleName((String) data[6]);

            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public UserDTO getShipperByShipperId(Integer shipperId) {

        User user = userRepo.findById(shipperId).orElseThrow(()->new RuntimeException("Shipper not Found !"));
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setAddress(user.getAddress());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setFullName(user.getFullName());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setRoleName("SHIPPER");
        return userDTO;
    }

    @Override
    public ShipperInForDTO updateShipperInformation(Integer shipperId, ShipperInForDTO shipperInForDTO,String avtUrl) {
        User user = userRepo.findById(shipperId).orElseThrow(()->new RuntimeException("Shipper not Found !"));
        user.setAddress(shipperInForDTO.getAddress());
        user.setEmail(shipperInForDTO.getEmail());
        user.setPhoneNumber(shipperInForDTO.getPhoneNumber());
        user.setFullName(shipperInForDTO.getFullName());
        user.setAvatar(avtUrl);
        try {
            userRepo.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return shipperInForDTO;
    }
}
