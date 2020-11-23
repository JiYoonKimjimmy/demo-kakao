package com.demo.kakao.controller;

import com.demo.kakao.entity.User;
import com.demo.kakao.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() { return userService.getUsers(); }

    @GetMapping(value = "/{userId}")
    public User getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public User save() {
        return userService.save();
    }

}
