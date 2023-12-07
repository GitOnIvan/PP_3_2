package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;


public interface UserService extends UserDetailsService {

    User findUsersByEmail(String email);


}


