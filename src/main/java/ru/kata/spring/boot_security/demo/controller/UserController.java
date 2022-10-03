package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;


import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getUserHomePage(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute(user);
        return "user";
    }

}
