package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {


    User findUsersByEmail(String email);

    List<User> getUsersByEmailNotNull();




}
