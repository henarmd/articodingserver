package com.articoding.service;

import com.articoding.RoleHelper;
import com.articoding.error.ErrorNotFound;
import com.articoding.error.NotAuthorization;
import com.articoding.model.ClassRoom;
import com.articoding.model.Role;
import com.articoding.model.UserForm;
import com.articoding.model.in.IUser;
import com.articoding.model.User;
import com.articoding.repository.ClassRepository;
import com.articoding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    RoleHelper roleHelper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;



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

    public Long save(UserForm user) {
        /** Comprobamos que sea, mínimo profesor */
        User actualUser = this.getActualUser();
        if(!roleHelper.can(actualUser.getRoles(),"ROLE_TEACHER")) {
            throw new  NotAuthorization("crear usuarios");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setEnabled(true);

        /** Añadimos los roles, comprobando que el usuario tiene permisos para asignarlos según su nivel */
        List<Role> roleList = new ArrayList<>();
        for (String role: user.getRoles()) {
            Role newRole = null;
            switch (role) {
                case "ROLE_ADMIN":{
                    if (roleHelper.isAdmin(actualUser)) {
                        newRole = roleHelper.getAdmin();
                    } else {
                        throw new NotAuthorization("crear usuarios ADMIN");
                    }
                } break;

                case "ROLE_TEACHER":{
                    if (roleHelper.isAdmin(actualUser)) {
                        newRole = roleHelper.getTeacher();
                    } else {
                        throw new NotAuthorization("crear usuarios PROFESOR");
                    }
                }break;

                case "ROLE_USER": {newRole = roleHelper.getUser();} break;

                default:
                    throw new ErrorNotFound("role", -1L);

            }
            roleList.add(newRole);
        }
        newUser.setRoles(roleList);

        /** Añadimos las clases, comprobando que el usuario es profesor de ellas*/
        List<ClassRoom> classRoomList = new ArrayList<>();
        for (Long idClass: user.getClasses()) {
            ClassRoom classRoom = classRepository.findById(idClass)
                    .orElseThrow(() -> new ErrorNotFound("clase", idClass));
            if (!classRoom.getTeachers().stream().anyMatch(t -> t.getId() == actualUser.getId())) {
                throw new NotAuthorization("crear alumnos en la clase " + idClass);
            }
            classRoom.getStudents().add(newUser);
            classRoomList.add(classRoom);
        }
        newUser.setClasses(classRoomList);

        User createdUser = userRepository.save(newUser);

        return createdUser.getId();
    }

    public Page<IUser> getUsers(Pageable pageable) {
        /** Comprobamos que sea, mínimo profesor */
        User actualUser = this.getActualUser();
        if(!roleHelper.can(actualUser.getRoles(),"ROLE_TEACHER")) {
            throw new  NotAuthorization("obtener usuarios");
        }
        /** Si es admin, mostramos todos */
        if(roleHelper.isAdmin(actualUser)) {
            return userRepository.findBy(pageable, IUser.class);
        } else {
            /** Si es profesor mostramos solo USER */
            return userRepository.findByRolesIn(pageable, Arrays.asList(roleHelper.getUser()));
        }

    }
    public Page<IUser> getUsersPerClass(PageRequest pageable, Long idClass) {

        /** Comprobamos que sea, mínimo profesor */
        User actualUser = this.getActualUser();
        if(!roleHelper.can(actualUser.getRoles(),"ROLE_TEACHER")) {
            throw new  NotAuthorization("obtener usuarios");
        }
        /** Comprobamos que exista la clase*/
        ClassRoom classRoom = classRepository.findById(idClass)
                .orElseThrow(() -> new ErrorNotFound("clase", idClass));

        /** Comprobamos que sea ADMIN o profesor de la clase en cuestion */
        if (!roleHelper.isAdmin(actualUser) && !classRoom.getTeachers().stream().anyMatch(t -> t.getId() == actualUser.getId())) {
            throw new NotAuthorization("obtener alumnos de la clase " + idClass);
        }

        return userRepository.findByClassRoomsIn(pageable, Arrays.asList(classRoom) , IUser.class);
    }
}
