package com.articoding;

import com.articoding.model.Role;
import com.articoding.model.RoleType;
import com.articoding.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleHelper {
    @Autowired
    RoleRepository roleRepository;

    Role ROOT;
    Role ADMIN;
    Role USER;

    public List<Role> getRole() {
        List<Role> roles = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<SimpleGrantedAuthority> privileges = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        List<String> actualUserRoles = privileges.stream().map(simpleGrantedAuthority -> simpleGrantedAuthority.getAuthority()).collect(Collectors.toList());
        if (actualUserRoles.contains("ROLE_ROOT")) {
            roles.add(this.roleRepository.findByName("ROLE_ROOT"));
            roles.add(this.roleRepository.findByName("ROLE_ADMIN"));
            roles.add(this.roleRepository.findByName("ROLE_USER"));
        }
        if (actualUserRoles.contains("ROLE_ADMIN")) {
            roles.add(this.roleRepository.findByName("ROLE_ADMIN"));
            roles.add(this.roleRepository.findByName("ROLE_USER"));
        }
        if (actualUserRoles.contains("ROLE_USER")){
            roles.add(this.roleRepository.findByName("ROLE_USER"));
        }
        return roles;
    }
}
