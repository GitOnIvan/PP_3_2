package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepo;
import ru.kata.spring.boot_security.demo.repository.UserRepo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class UserService {


    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }


    // Role methods

    public List<Role> findAll() {

        return roleRepo.findAll();
    }

    @Transactional
    public void addRole(Role role) {
        roleRepo.save(role);
    }


    // User methods


    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }


    public List<User> findAllUsers() {
        return userRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .distinct()
                .collect(Collectors.toList());


    }


    @Transactional
    public User addNewUser(User user) {

        if ((userRepo.findUserByEmail(user.getEmail())) == null) {
            Set<Role> rolesFinal = user.getRole().stream()
                    .flatMap(role2 -> roleRepo.findAll().stream()
                            .filter(role1 -> role1.getName().equals(role2.getName()))
                            .map(role1 -> new Role(role1.getId(), role1.getName())))
                    .collect(Collectors.toSet());

            user.setRole(rolesFinal);
            user.setPass(passwordEncoder.encode(user.getPass()));
            userRepo.save(user);
            return user;
        } else {
            throw new EntityExistsException("User is already exists!!!");
        }


    }


    @Transactional
    public void deleteById(Long id) {

        if (userRepo.findUsersById(id) == null) {
            throw new EntityNotFoundException("User is not found!!!");
        }
        userRepo.deleteById(id);


    }


    @Transactional
    public User findUsersById(Long id) {

        if (userRepo.findUsersById(id) == null) {
            throw new EntityNotFoundException("User is not found!!!");
        }

        return userRepo.findUsersById(id);
    }


    @Transactional
    public User updateUser(long id, User updatedUser) {

        if ((userRepo.findUserByEmail(updatedUser.getEmail()).getId() != id)
                && (userRepo.findUserByEmail(updatedUser.getEmail()) != null)) {

            throw new EntityExistsException("User is already exists!!!");

        } else {

            User userToBeUpdate = userRepo.findUsersById(id);

            Set<Role> rolesFinal = updatedUser.getRole().stream()
                    .flatMap(role2 -> roleRepo.findAll().stream()
                            .filter(role1 -> role1.getName().equals(role2.getName()))
                            .map(role1 -> new Role(role1.getId(), role1.getName())))
                    .collect(Collectors.toSet());


            userToBeUpdate.setFirstName(updatedUser.getFirstName());
            userToBeUpdate.setLastName(updatedUser.getLastName());
            userToBeUpdate.setAge(updatedUser.getAge());
            userToBeUpdate.setGender(updatedUser.getGender());
            userToBeUpdate.setEmail(updatedUser.getEmail());
            userToBeUpdate.setPass(passwordEncoder.encode(updatedUser.getPass()));
            userToBeUpdate.setRole(rolesFinal);
            userToBeUpdate.setStatus(updatedUser.getStatus());

            return userToBeUpdate;
        }





    }


}