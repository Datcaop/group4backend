package org.fai.study.projectsem4.service.Impl;

import org.fai.study.projectsem4.entity.User;
import org.fai.study.projectsem4.entity.UserDetailsImpl;
import org.fai.study.projectsem4.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    public UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found with Username" + username));
        return UserDetailsImpl.build(user);
    }
}
