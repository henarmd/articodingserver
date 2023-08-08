package com.articoding.service;

import com.articoding.RoleHelper;
import com.articoding.error.ErrorNotFound;
import com.articoding.error.NotAuthorization;
import com.articoding.model.ClassRoom;
import com.articoding.model.Level;
import com.articoding.model.User;
import com.articoding.model.in.*;
import com.articoding.repository.ClassRepository;
import com.articoding.repository.LevelRepository;
import com.articoding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassService {

    @Autowired
    UserService userService;

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

    public IClassRoomDetail getById(Long classId) {

        User actualUser = userService.getActualUser();
        /** Comprobamos que existe la clase */
        ClassRoom classRoom = classRepository.findById(classId)
                .orElseThrow(()-> new ErrorNotFound("clase", classId));
        /** Comprobamos que es ADMIN o alumno o profesor de la clase */
        if (!roleHelper.isAdmin(actualUser) && !classRoom.getStudents().stream().anyMatch(s -> s.getId() == actualUser.getId()) &&
                !classRoom.getTeachers().stream().anyMatch(t-> t.getId() == actualUser.getId())) {
            throw new NotAuthorization("acceder a la clase con id " + classId);
        }
        return classRepository.findById(classId, IClassRoomDetail.class);
    }

    public Page<IClassRoom> getClasses(PageRequest pageRequest, Optional<Long> userId, Optional<Long> teachId, Optional<Long> levelId) {
        /** Si quiere saber las clases de un usuario o de un nivel, debe ser minimo profesor*/
        User actualUser = userService.getActualUser();
        if(userId.isPresent() || teachId.isPresent() || levelId.isPresent()) {
            if(!roleHelper.can(actualUser.getRoles(), "ROLE_TEACHER")) {
                throw new NotAuthorization("get class of user ");
            } else {/** Deveulvo las clases de las que es usuario o profesor */
                if(userId.isPresent()) {
                    return classRepository.findByStudentsId( userId.get(),pageRequest, IClassRoom.class);
                } else if (teachId.isPresent()){
                    return classRepository.findByTeachersId( teachId.get(),pageRequest, IClassRoom.class);
                } else {
                    return classRepository.findByLevelsId( levelId.get(),pageRequest, IClassRoom.class);
                }
            }
        } else {
            if(roleHelper.isAdmin(actualUser)) {
                /** Si es ADMIN, devuelve TODAS las clases*/
                return classRepository.findBy(pageRequest, IClassRoom.class);
            } else if (roleHelper.isTeacher(actualUser)) {
                /** Si es profe devuelve todas las clases donde es profesor*/
                return classRepository.findByTeachersId( actualUser.getId(),pageRequest, IClassRoom.class);
            } else {
                /** Si es alumno, deveulve las clases en las que es alumno */
                return classRepository.findByStudentsId(actualUser.getId(),pageRequest, IClassRoom.class);
            }
        }
    }

    public Long updateClassRoom(Long classId, UpdateClassRoomForm updateClassRoomForm) {

        ClassRoom classRoom = canEdit(classId);

        /** Se modifican los campos informados*/
        if (updateClassRoomForm.getName() != null) {
            classRoom.setName(updateClassRoomForm.getName());
        }

        if (updateClassRoomForm.getDescription() != null) {
            classRoom.setDescription(updateClassRoomForm.getDescription());
        }

        if (updateClassRoomForm.isEnabled() != null) {
            classRoom.setEnabled(updateClassRoomForm.isEnabled());
        }

        classRepository.save(classRoom);
        return  classRoom.getId();
    }

    public Long addLevel(Long classId, List<IUid> levelsId) {
        ClassRoom classRoom = canEdit(classId);

        for(IUid levelId : levelsId ){
            Level level = levelRepository.findById(levelId.getId()).
                    orElseThrow(() -> new ErrorNotFound("Nivel", levelId.getId()));
            /** Si ya es parte de la clase no hago nada*/
            if(classRoom.getLevels().stream().anyMatch(level1 -> level1.getId() == level.getId())) {
                return classId;
            }

            level.getClassRooms().add(classRoom);
            classRoom.getLevels().add(level);
        }

        classRepository.save(classRoom);

        return classRoom.getId();
    }

    public Long deleteLevel(Long classId, Long levelId) {
        ClassRoom classRoom = canEdit(classId);

        Level level = levelRepository.findById(levelId).
                orElseThrow(() -> new ErrorNotFound("Nivel", levelId));
        /** Si no es parte de la clase no hago nada*/
        if(!classRoom.getLevels().stream().anyMatch(level1 -> level1.getId() == level.getId())) {
            return classId;
        }

        List<Level> actualLevels = classRoom.getLevels().stream().filter(level1 -> level1.getId() != level.getId()).collect(Collectors.toList());
        classRoom.setLevels(actualLevels);
        classRepository.save(classRoom);

        return classRoom.getId();
    }

    private ClassRoom canEdit(Long classId){

        User actualUser = userService.getActualUser();

        /** Obtenemos la vieja clase*/
        ClassRoom classRoom = classRepository.findById(classId).
                orElseThrow(() -> new ErrorNotFound("clase", classId ));

        /** Se verifica si es admin o profesor de la clase*/
        if(!roleHelper.isAdmin(actualUser) && !classRoom.getTeachers().stream().anyMatch(t -> t.getId() == classId)) {
            throw new NotAuthorization("Modificar la clase " + classId);
        }

        return classRoom;
    }
}
