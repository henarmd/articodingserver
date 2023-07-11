package com.articoding.service;

import com.articoding.RoleHelper;
import com.articoding.model.ClassRoom;
import com.articoding.model.IUser;
import com.articoding.model.User;
import com.articoding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class UserService {

    @Autowired
    RoleHelper roleHelper;

    @Autowired
    UserRepository userRepository;

    public Page<IUser> getUsers(Pageable pageable) {
        return userRepository.findByRolesIn(pageable, roleHelper.getRole());
    }

    public List<ClassRoom> getClasses(Long userId) {
        User user = userRepository.getOne(userId);
        if(user == null) {
            throw new RuntimeException("eso no existe loco");
        }
        return null;
    }

    public User getActualUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }
}
