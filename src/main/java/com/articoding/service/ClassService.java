package com.articoding.service;

import com.articoding.RoleHelper;
import com.articoding.error.ErrorNotFound;
import com.articoding.error.NotAuthorization;
import com.articoding.model.ClassRoom;
import com.articoding.model.User;
import com.articoding.model.in.ClassForm;
import com.articoding.model.in.IClassRoom;
import com.articoding.model.in.ILevel;
import com.articoding.repository.ClassRepository;
import com.articoding.repository.LevelRepository;
import com.articoding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    LevelRepository levelRepository;
    @Autowired
    RoleHelper roleHelper;

    public ClassRoom createClass(User actualUser, ClassForm classForm) {

        /** Comprobamos que sea profesor */
        if(!roleHelper.isTeacher(actualUser)) {
            throw new NotAuthorization("Sin el role teacher no se pueden crear clases");
        }

        ClassRoom newClassRoom = new ClassRoom();
        newClassRoom.setDescription(classForm.getDescription());
        newClassRoom.setName(classForm.getName());
        List<User> students = new ArrayList<>();
        classForm.getStudentsId().forEach(studentId -> {
            Optional<User> userOptional = userRepository.findById(studentId);
            if (userOptional.isPresent()) {
                students.add(userOptional.get());
            } else {
                throw new ErrorNotFound("usuario", studentId);
            }
        });
        newClassRoom.setStudents(students);

        List<User> teachers = new ArrayList<>();
        classForm.getTeachersId().forEach(studentId -> {
            Optional<User> userOptional = userRepository.findById(studentId);
            if (userOptional.isPresent()) {
                if( roleHelper.isTeacher(userOptional.get())) {
                    teachers.add(userOptional.get());
                } else {
                    throw new NotAuthorization("Uno de los profesores de la clase, no está autorizado");
                }
            } else {
                throw new ErrorNotFound("usuario", studentId);
            }
        });
        teachers.add(actualUser);
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

    public List<ILevel> getAllLevels(User actualUser, Long classId) {
        /** Comprobamos que existe la clase */
        ClassRoom classRoom = classRepository.findById(classId)
                .orElseThrow(()-> new ErrorNotFound("clase", classId));
        /** Comprobamos que es alumno o profesor de la clase */
        if (!classRoom.getStudents().stream().anyMatch(s -> s.getId() == actualUser.getId()) &&
                !classRoom.getTeachers().stream().anyMatch(t-> t.getId() == actualUser.getId())) {
            throw new NotAuthorization("acceder a la clase con id " + classId);
        }
        return levelRepository.findByClassRooms(classRoom, ILevel.class);
    }

    public IClassRoom getById(User actualUser, Long classId) {
        /** Comprobamos que existe la clase */
        ClassRoom classRoom = classRepository.findById(classId)
                .orElseThrow(()-> new ErrorNotFound("clase", classId));
        /** Comprobamos que es alumno o profesor de la clase */
        if (!classRoom.getStudents().stream().anyMatch(s -> s.getId() == actualUser.getId()) &&
                !classRoom.getTeachers().stream().anyMatch(t-> t.getId() == actualUser.getId())) {
            throw new NotAuthorization("acceder a la clase con id " + classId);
        }
        return classRepository.findById(classId, IClassRoom.class);
    }
}
