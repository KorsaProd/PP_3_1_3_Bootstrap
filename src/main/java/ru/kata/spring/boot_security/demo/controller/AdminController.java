package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userService;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Model model, Principal principal) {
        model.addAttribute("user",
                userService.findByUsername(principal.getName()));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping("/newUser")
    public String createUser(User user, String role) {
        userService.saveUser(user, role);
        return "redirect:/admin";
    }


    @PostMapping("/{id}")
    public String update(@ModelAttribute("newUser") User newUser, String role,
                         @PathVariable Integer id) {
        userService.edit(newUser, id, role);
        return "redirect:/admin";
    }

    @GetMapping("deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }


    @PostMapping ("/deleteUser/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

}
