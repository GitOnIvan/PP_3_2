package ru.kata.spring.boot_security.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }




    @GetMapping("/users")
    public List<User> getUserList() {

        return userService.findAllUsers();
    }



    @GetMapping("/users/{userId}")
    public User getById(@PathVariable("userId") long userId) {

        return userService.findUsersById(userId);
    }



    @PostMapping("/users")
    public User addNewUser(@RequestBody User user) {
        return userService.addNewUser(user);
    }


    @PutMapping("/users")
    public User apdateUser(@RequestBody User user, @RequestParam long id) {
        return userService.updateUser(id, user);


    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);


    }


}

