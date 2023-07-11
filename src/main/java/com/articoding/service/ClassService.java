package com.articoding.service;

import com.articoding.model.ClassRoom;
import com.articoding.model.User;
import com.articoding.model.in.ClassForm;
import com.articoding.model.in.IClassRoom;
import com.articoding.repository.ClassRepository;
import com.articoding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClassService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassRepository classRepository;


    public ClassRoom createClass(User owner, ClassForm classForm) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClassRoom newClassRoom = new ClassRoom();
        newClassRoom.setDescription(classForm.getDescription());
        newClassRoom.setName(classForm.getName());
        List<User> students = new ArrayList<>();
        classForm.getStudentsId().forEach(studentId -> {
            Optional<User> userOptional = userRepository.findById(studentId);
            if (userOptional.isPresent()) {
                students.add(userOptional.get());
            } else {
                throw new RuntimeException("eSTE CABALLERO NO EXISTE");
            }
        });
        newClassRoom.setStudents(students);

        List<User> teachers = new ArrayList<>();
        classForm.getTeachersId().forEach(studentId -> {
            Optional<User> userOptional = userRepository.findById(studentId);
            if (userOptional.isPresent()) {
                teachers.add(userOptional.get());
            } else {
                throw new RuntimeException("eSTE CABALLERO NO EXISTE");
            }
        });
        teachers.add(owner);
        newClassRoom.setTeachers(teachers);

        return classRepository.save(newClassRoom);

    }

    public List<IClassRoom> getUserClasses(Long idUser) {
        return classRepository.findByStudentsId(idUser);
    }

    public List<IClassRoom> getTeacherClasses(Long idUser) {
        return classRepository.findByTeachersId(idUser);
    }

    public List<IClassRoom> getAllClassesPerUser(User userOwner, boolean student) {
        Long userId = userOwner.getId();
        if (student) {
            return classRepository.findByStudentsId(userId);
        } else {
            return classRepository.findByTeachersId(userId);
        }
    }
}
