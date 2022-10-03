package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

 private final UserRepository userRepository;
 private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

 @Autowired
 public UserServiceImpl(UserRepository userRepository) {
  this.userRepository = userRepository;
 }

 public List<User> getAllUsers() {
  return userRepository.findAll();
 }

 @Transactional
 public User saveUser(User user, String role) {
  User newUser = new User();
  Set<Role> roles;
  if (role.equals("ROLE_ADMIN")) {
   roles = Set.of(new Role(user.getId(), "ROLE_USER"), new Role(user.getId(), "ROLE_ADMIN"));
  } else {
   roles = Set.of(new Role(user.getId(), "ROLE_USER"));
  }
  newUser.setRoles(roles);
  newUser.setName(user.getName());
  newUser.setLastName(user.getLastName());
  newUser.setAge(user.getAge());
  newUser.setPassword(user.getPassword());
  newUser.setRoles(roles);
  newUser.setId(user.getId());
  newUser.setPassword(passwordEncoder.encode(user.getPassword()));
  return userRepository.save(newUser);
 }

 @Transactional
 public void edit(User user, Integer id, String role) {
  Set<Role> rolesToChange;
  if (role.equals("ROLE_ADMIN")) {
   rolesToChange = Set.of(new Role(1, "ROLE_USER"),new Role(2, "ROLE_ADMIN"));
  } else {
   rolesToChange = Set.of(new Role(1, "ROLE_USER"));
  }

  User editUser = new User();
  editUser.setName(user.getName());
  editUser.setLastName(user.getLastName());
  editUser.setAge(user.getAge());
  editUser.setRoles(rolesToChange);
  editUser.setId(id);
  editUser.setPassword(passwordEncoder.encode(user.getPassword()));
  userRepository.save(editUser);
 }

 public void removeUserById(Integer id) {
  userRepository.deleteById(id);
 }

 public User getUserById(Integer id) {
  return userRepository.getById(id);
 }

 public User findByUsername(String username) {
  return userRepository.findByUsername(username);
 }

 @Override
 @Transactional
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  User user = findByUsername(username);
  if (user == null) {
   throw new UsernameNotFoundException(String.format("User '%s' not found", username));
  }
  return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
          mapRolesToAuthorities(user.getRoles()));
 }

 private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
  return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
 }

 //Добавление пользователя для теста функционала
 @PostConstruct
 public User createTestAdmin() {
  if (userRepository.findByUsername("admin") == null) {
   User user = new User("admin", "test", 30);
   user.setId(1);
   user.setPassword(passwordEncoder.encode("admin"));
   user.addRole(new Role(user.getId(), "ROLE_ADMIN"));
   user.addRole(new Role(user.getId(), "ROLE_USER"));
   return userRepository.save(user);
  }
  return null;
 }
}
