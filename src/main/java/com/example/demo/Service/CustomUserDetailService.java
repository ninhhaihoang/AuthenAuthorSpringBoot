package com.example.demo.Service;

import com.example.demo.Model.UserEntity;
import com.example.demo.Repository.RoleDAO;
import com.example.demo.Repository.UserDAO;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        UserEntity userEntity = this.userDAO.findUserAccount(email);

        if(user == null) {
            System.out.println("User not found! " + email);
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("Found User: " + userEntity);

        // [ROLE_USER, ROLE_ADMIN,..]
        List<String> roleNames = this.roleDAO.getRoleNames((long) userEntity.getUserId()); // xoá (long) ?

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (roleNames != null) {
            for (String role : roleNames) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }

        UserDetails userDetails = (UserDetails) new User(userEntity.getEmail(),
                userEntity.getPassword(), grantList);

        return userDetails;
        //return new CustomUserDetail(user); // cũ

    }
}
