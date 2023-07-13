package com.articoding.controller;

import com.articoding.model.UserForm;
import com.articoding.model.in.IUser;
import com.articoding.model.in.IClassRoom;
import com.articoding.model.rest.CreatedRef;
import com.articoding.service.ClassService;
import com.articoding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ClassService classService;

    @GetMapping
    public Page<IUser> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "10") int size,
                                @RequestParam(name = "clase", required=false) Optional<Long> clase) throws Exception {
        if(clase.isPresent()) {
            Page<IUser> resultPage = userService.getUsersPerClass(PageRequest.of(page, size),clase.get());
            return resultPage;
        } else {
            Page<IUser> resultPage = userService.getUsers(PageRequest.of(page, size));
            return resultPage;
        }

    }
    @GetMapping(value = "/{userId}/classes")
    public List<IClassRoom> getUsers(@PathVariable(value="userId") Long userId) {
        return classService.getUserClasses(userId);
    }

    @PostMapping
    public ResponseEntity<CreatedRef> saveUser(@RequestBody UserForm user) throws Exception {
        return ResponseEntity.ok(new CreatedRef("users/" +userService.save(user)));
    }

}
