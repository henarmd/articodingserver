package com.articoding.controller;

import com.articoding.model.ClassRoom;
import com.articoding.model.in.ClassForm;
import com.articoding.model.in.IClassRoom;
import com.articoding.service.ClassService;
import com.articoding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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
    public ResponseEntity<Page<IClassRoom>> getAllClass(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "user", required=false) Optional<Long> userId,
            @RequestParam(name = "teacher", required=false) Optional<Long> teachId,
            @RequestParam(name = "level",required=false) Optional<Long> levelId){
        return ResponseEntity.ok(classService.getClasses(PageRequest.of(page, size), userId, teachId, levelId));
    }

    @GetMapping("/{classId}")
    public ResponseEntity<IClassRoom> getById(@PathVariable(value="classId") Long classId){
        return ResponseEntity.ok(classService.getById(classId));
    }
}
