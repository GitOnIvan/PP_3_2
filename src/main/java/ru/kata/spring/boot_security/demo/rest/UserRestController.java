package ru.kata.spring.boot_security.demo.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@RestController
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user/api/v1/user")
    public ResponseEntity<User> getByEmail(Principal principal) {

        return new ResponseEntity<>(userService.findUserByEmail(principal.getName()), HttpStatus.OK);
    }


















}

