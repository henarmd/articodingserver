package com.articoding;

import com.articoding.model.Role;
import com.articoding.model.RoleType;
import com.articoding.model.User;
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
        if (actualUserRoles.contains("ROLE_ADMIN")) {
            roles.add(this.roleRepository.findByName("ROLE_ADMIN"));
            roles.add(this.roleRepository.findByName("ROLE_TEACHER"));
            roles.add(this.roleRepository.findByName("ROLE_USER"));
        }
        if (actualUserRoles.contains("ROLE_TEACHER")) {
            roles.add(this.roleRepository.findByName("ROLE_TEACHER"));
            roles.add(this.roleRepository.findByName("ROLE_USER"));
        }
        if (actualUserRoles.contains("ROLE_USER")){
            roles.add(this.roleRepository.findByName("ROLE_USER"));
        }
        return roles;
    }

    public Role getMaxRole(List<Role> roles) {
        if (roles.stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
           return roles.stream().filter(r -> r.getName().equals("ROLE_ADMIN")).findFirst().get();
        } else if  (roles.stream().anyMatch(r -> r.getName().equals("ROLE_TEACHER"))) {
            return roles.stream().filter(r -> r.getName().equals("ROLE_TEACHER")).findFirst().get();
        } else if (roles.stream().anyMatch(r -> r.getName().equals("ROLE_USER"))) {
            return roles.stream().filter(r -> r.getName().equals("ROLE_USER")).findFirst().get();
        } else {
            throw new RuntimeException("Esto es raro...");
        }
    }

    public boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
    }
    public boolean can(List<Role> roles, String necesaryRoot) {

        if (necesaryRoot.equals("ROLE_ADMIN")) {
            return roles.stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
        }
        if (necesaryRoot.equals("ROLE_TEACHER")) {
            return roles.stream().anyMatch(r ->
                    r.getName().equals("ROLE_ADMIN") ||
                            r.getName().equals("ROLE_TEACHER"));
        }
        if (necesaryRoot.equals("ROLE_USER")) {
            return roles.stream().anyMatch(r ->
                    r.getName().equals("ROLE_ADMIN") ||
                            r.getName().equals("ROLE_TEACHER") ||
                            r.getName().equals("ROLE_USER")) ;
        }
        return false;
    }

    public boolean isTeacher(User actualUser) {
        return actualUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_TEACHER"));
    }
}
