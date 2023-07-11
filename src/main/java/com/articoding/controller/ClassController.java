package com.articoding.controller;

import com.articoding.model.ClassRoom;
import com.articoding.model.in.ClassForm;
import com.articoding.model.in.IClassRoom;
import com.articoding.service.ClassService;
import com.articoding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClassController {
    @Autowired
    ClassService classService;
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<ClassRoom> createClass(@RequestBody ClassForm classForm) throws Exception {
        return ResponseEntity.ok(classService.createClass(userService.getActualUser(), classForm));
    }

    @GetMapping
    public List<IClassRoom> getAllClass(@RequestParam(defaultValue = "true") boolean student){
        return classService.getAllClassesPerUser(userService.getActualUser(), student);
    }

    @GetMapping("/{classId}/levels")
    public List<IClassRoom> getClass(@PathVariable(value="classId") Long classId){
        throw new RuntimeException("Esto aún no esta..");
    }
}
