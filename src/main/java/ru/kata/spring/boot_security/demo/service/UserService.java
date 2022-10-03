package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();
    User saveUser(User user, String role);
    void removeUserById(Integer id);
    User getUserById(Integer id);
    User findByUsername(String username);
}
