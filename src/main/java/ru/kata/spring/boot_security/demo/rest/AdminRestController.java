package ru.kata.spring.boot_security.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/api/v1/users")
    public ResponseEntity<List<User>> getUserList() {

        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/api/v1/users/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
        return "User with ID " + userId + " was deleted!";

    }


    @PutMapping("/admin/api/v1/users")
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestParam long id) {


        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);

    }

    @PostMapping("/admin/api/v1/users")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {

        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.OK);

    }


    @GetMapping("/admin/api/v1/users/{userId}")
    public ResponseEntity<User> getById(@PathVariable("userId") long userId) {

        return new ResponseEntity<>(userService.findUsersById(userId), HttpStatus.OK);
    }





}
