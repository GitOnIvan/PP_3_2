package ru.kata.spring.boot_security.demo.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> getUserList() {

        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }



    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getById(@PathVariable("userId") long userId) {

        return new ResponseEntity<>(userService.findUsersById(userId), HttpStatus.OK);
    }



    @PostMapping("/users")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.OK);

    }


    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestParam long id) {

        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);



    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
        return "User with ID " + userId + " was deleted!";

    }


}

