package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepo;
import ru.kata.spring.boot_security.demo.repository.UserRepo;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {


    @PersistenceContext
    private EntityManager em;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with '%s' not found", email));
        }
        return user;
    }

    // Role methods
    @Transactional
    public List<Role> findAll() {

        return roleRepo.findAll();
    }

    @Transactional
    public void addRole(Role role) {
        roleRepo.save(role);
    }


    // User methods

    @Transactional
    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }


    @Transactional
    public List<User> findAllUsers() {
        return userRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .distinct()
                .collect(Collectors.toList());


    }

    @Transactional
    public User addNewUser(User user) {

        Set<Role> rolesFinal = user.getRole().stream()
                .flatMap(role2 -> roleRepo.findAll().stream()
                        .filter(role1 -> role1.getName().equals(role2.getName()))
                        .map(role1 -> new Role(role1.getId(), role1.getName())))
                .collect(Collectors.toSet());

        user.setRole(rolesFinal);
        user.setPass(passwordEncoder.encode(user.getPass()));
        userRepo.save(user);
        return user;
    }

    @Transactional
    public void deleteById(Long id) {
        userRepo.deleteById(id);


    }


    @Transactional
    public User findUsersById(Long id) {
        return userRepo.findUsersById(id);
    }


    @Transactional
    public User updateUser(long id, User updatedUser) {
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