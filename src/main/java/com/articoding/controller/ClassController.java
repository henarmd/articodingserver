package com.articoding.controller;

import com.articoding.model.ClassRoom;
import com.articoding.model.in.ClassForm;
import com.articoding.model.in.IClassRoom;
import com.articoding.model.in.ILevel;
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
    public ResponseEntity<List<IClassRoom>> getAllClass(@RequestParam(defaultValue = "true") boolean student){
        return ResponseEntity.ok(classService.getAllClassesPerUser(userService.getActualUser(), student));
    }

    @GetMapping("/{classId}")
    public ResponseEntity<IClassRoom> getById(@PathVariable(value="classId") Long classId){
        return ResponseEntity.ok(classService.getById(userService.getActualUser(), classId));
    }

    @GetMapping("/{classId}/levels")
    public ResponseEntity<List<ILevel>> getClass(@PathVariable(value="classId") Long classId){
        return ResponseEntity.ok(classService.getAllLevels(userService.getActualUser(), classId));
    }
}
