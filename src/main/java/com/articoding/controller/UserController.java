package com.articoding.controller;

import com.articoding.model.ClassRoom;
import com.articoding.model.IUser;
import com.articoding.model.in.IClassRoom;
import com.articoding.service.ClassService;
import com.articoding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ClassService classService;

    @GetMapping
    public Page<IUser> getUsers(@RequestParam("page") int page,
                                @RequestParam("size") int size) throws Exception {
        Page<IUser> resultPage = userService.getUsers(PageRequest.of(page, size));
        if (page > resultPage.getTotalPages()) {
            throw new Exception("No existe maquina");
        }
        return resultPage;
    }
    @GetMapping(value = "/{userId}/classes")
    public List<IClassRoom> getUsers(@PathVariable(value="userId") Long userId) {
        return classService.getUserClasses(userId);
    }


}
