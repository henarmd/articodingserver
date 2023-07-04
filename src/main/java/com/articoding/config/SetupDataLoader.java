package com.articoding.config;

import com.articoding.repository.RoleRepository;
import com.articoding.repository.UserRepository;
import com.articoding.model.Role;
import com.articoding.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private RoleRepository roleRepository;



    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createRoleIfNotFound("ROLE_ROOT");
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");


        Role adminRole = roleRepository.findByName("ROLE_ROOT");
        if (adminRole.getUsers().isEmpty()) {
            User user = new User();
            user.setUsername("root");
            user.setPassword(bcryptEncoder.encode("root"));
            user.setEnabled(true);
            user.setRoles(Arrays.asList(adminRole));
            userRepository.save(user);
        }



        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }
}