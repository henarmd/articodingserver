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
import com.articoding.repository.ClassRepository;
import com.articoding.repository.LevelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LevelService {

    @Autowired
    LevelRepository levelRepository;

    @Autowired
    ClassRepository classRepository;

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
            if (!roleHelper.can(actualUser.getRoles(), "ROLE_TEACHER")) {
                throw new NotAuthorization(roleHelper.getMaxRole(actualUser.getRoles()),
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

        if(!level.isActive()) {
            throw new DisabledEntity("nivel");
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

    public Page<ILevel> getLevels(User actualUser, Pageable pageable) {
        /** Si es ADMIN devuelve todos */
        if(roleHelper.isAdmin(actualUser)) {
            return levelRepository.findBy(pageable, ILevel.class);
        } else {
            return levelRepository.findByOwnerAndActiveTrue(actualUser, pageable, ILevel.class);
        }
    }
}
