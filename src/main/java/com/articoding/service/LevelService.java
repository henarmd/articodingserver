package com.articoding.service;

import com.articoding.RoleHelper;
import com.articoding.error.DisabledEntity;
import com.articoding.error.ErrorNotFound;
import com.articoding.error.NotAuthorization;
import com.articoding.model.ClassRoom;
import com.articoding.model.Level;
import com.articoding.model.User;
import com.articoding.model.in.ILevel;
import com.articoding.model.in.LevelForm;
import com.articoding.model.in.UpdateLevelForm;
import com.articoding.repository.ClassRepository;
import com.articoding.repository.LevelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LevelService {

    @Autowired
    LevelRepository levelRepository;

    @Autowired
    ClassRepository classRepository;
    @Autowired
    UserService userService;
    @Autowired
    RoleHelper roleHelper;

    public Long createLevel(User actualUser, LevelForm levelForm) throws ErrorNotFound, NotAuthorization, JsonProcessingException {

        Level level = new Level();
        /**
         * Si el nivel pertenece a una clase, debe ser profesor de todas
         */
        if (!levelForm.getClasses().isEmpty()) {
            List<ClassRoom> classRoomList = new ArrayList<>();
            /** Comprobamos si es profesor */
            if (!roleHelper.can(actualUser.getRole(), "ROLE_TEACHER")) {
                throw new NotAuthorization(actualUser.getRole(),
                        "crear un nivel en una clase");
            }

            /** Comprobamos que las clases sean válidas */
            for (Long idClass : levelForm.getClasses()) {
                /** Controlamos que la clase exista */
                ClassRoom classRoom = classRepository.findById(idClass)
                        .orElseThrow(() -> new ErrorNotFound("Clase", idClass));

                /** Controlamos que sea profesor */
                if (! classRoom.getTeachers().contains(actualUser)) {
                    throw new NotAuthorization(" crear un nivel en la clase " + idClass);
                }
                classRoomList.add(classRoom);
                classRoom.getLevels().add(level);
            }
            level.setClassRooms(classRoomList);
        }

        level.setDescription(levelForm.getDescription());
        level.setTitle(levelForm.getTitle());
        level.setPublicLevel(levelForm.isPublicLevel());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        level.setSerializaArticodeingLevel(ow.writeValueAsString(levelForm.getArticodingLevel()));

        level.setOwner(actualUser);
        Level newLevel = levelRepository.save(level);

        return newLevel.getId();
    }

    public ILevel getLevel(User actualUser, Long levelId) {
        /** Comprobamos si existe la clase */
        Level level = levelRepository.findById(levelId, Level.class);
        if(level == null) {
            throw new ErrorNotFound("nivel", levelId);
        }

        if(!level.isActive() && !roleHelper.isAdmin(actualUser) && !(roleHelper.isTeacher(actualUser) && level.getOwner().getId() == actualUser.getId() )) {
            throw new NotAuthorization("nivel desactivado");
        }
        boolean canShow = false;
        /** Comprobamos si tiene permisos para verlo*/
        if(level.isPublicLevel()) {
            if (level.getOwner().getId() != actualUser.getId() && !roleHelper.isAdmin(actualUser)) {
                for (ClassRoom classRoom : level.getClassRooms()) {
                    if (!classRoom.getTeachers().stream().anyMatch(teacher -> teacher.getId() == actualUser.getId())) {
                        if(classRoom.getStudents().stream().anyMatch(studend -> studend.getId() == actualUser.getId())) {
                           /** Es alumno de alguna de las clases*/
                           canShow= true;
                        }
                    } else {/** Es profesor de alguna de las clases en las que está incluido el nivel */
                        canShow = true;
                    }
                }
            } else {/** es el dueño o ROOT*/
                canShow = true;
            }
        } else {/** es publico */
            canShow = true;
        }
        if(!canShow) {
            throw new NotAuthorization(String.format("ver la clase %s", level.getId()));
        }
        return levelRepository.findById(levelId, ILevel.class);

    }

    public Page<ILevel> getLevels(PageRequest pageRequest, Optional<Long> classId, Optional<Long> userId) {
        User actualUser = userService.getActualUser();
        /** Si quiere todos los niveles de una clase*/
        if(classId.isPresent()) {
            /** Comrpobamos que exista la clase*/
            ClassRoom classRoom = classRepository.findById(classId.get())
                    .orElseThrow(() -> new ErrorNotFound("clase", classId.get()));

            /**comprobamos que es su alumno/profesor o tiene role admin*/
            if(!roleHelper.isAdmin(actualUser)) {
                if (!classRoom.getStudents().stream().anyMatch(s -> s.getId() == actualUser.getId()) &&
                        !classRoom.getTeachers().stream().anyMatch(s -> s.getId() == actualUser.getId())) {
                    throw new NotAuthorization("ver niveles de la clase " + classId.get());
                }
            }
            return levelRepository.findByClassRoomsAndActiveTrue(classRoom, pageRequest, ILevel.class);
        } else if (userId.isPresent()) {
            /** Si quiere todos los niveles de un usuario, comprobamos que sea ADMIN */
            if(!roleHelper.isAdmin(actualUser)) {
                throw new NotAuthorization("ver niveles del usuario " + userId.get());
            } else {
                return levelRepository.findByOwnerAndActiveTrue(actualUser, pageRequest, ILevel.class);
            }
        } else {
            /** Si quiere todos los niveles*/
            if(roleHelper.isAdmin(actualUser)) {
                /** Si es admin devuelve todos*/
                return levelRepository.findBy(pageRequest, ILevel.class);
            } else {
                /**Si no, devuelve los creados por el usuario*/
                return levelRepository.findByOwnerAndActiveTrue(actualUser, pageRequest, ILevel.class);
            }
        }
     }

    public long updateLevel(UpdateLevelForm newLevel, Long levelId) {
        /** Comprobamos que existe el nivel */
        Level levelOld = levelRepository.findById(levelId)
                .orElseThrow(() -> new ErrorNotFound("level", levelId));

        User actualUser = userService.getActualUser();
        /** Comprobamos que sea el dueñó del nivel o usuario ADMIN */
        if (!actualUser.getCreatedLevels().stream().anyMatch(x -> x.getId() == levelId) &&
                !roleHelper.isAdmin(actualUser)) {
            throw new NotAuthorization("modificar el nivel " + levelId);
        } else {
            /** Podemos modificar */
            if (newLevel.getTitle() != null ) {
                levelOld.setTitle(newLevel.getTitle());
            }
            if (newLevel.getDescription() != null ) {
                levelOld.setDescription(newLevel.getDescription());
            }
            if (newLevel.isPublicLevel() != null ) {
                levelOld.setPublicLevel(newLevel.isPublicLevel());
            }
            if (newLevel.isActive() != null ) {
                levelOld.setActive(newLevel.isActive());
            }

            return levelRepository.save(levelOld).getId();

        }
    }
}
