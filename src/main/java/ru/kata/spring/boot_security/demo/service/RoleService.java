package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    public Set<Role> getRoleSetFromString(User userNeedsForRoleId, String str) {
        String[] roles = str.split(",");
        Set<Role> roleSet = new HashSet<>();
        for (String s : roles) {
            roleSet.add(new Role(userNeedsForRoleId.getId(), s));
        }
        return roleSet;
    }

}
