package ru.kata.spring.boot_security.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query(value = "FROM User u JOIN FETCH u.role r WHERE  u.email = :email")
    User findUserByEmail(String email);

    @Query(value = "FROM User u LEFT JOIN FETCH u.role r")
    List<User> findAll();

    @Query(value = "FROM User u JOIN FETCH u.role r WHERE  u.id = :id")
    User findUsersById(Long id);

    void deleteById(Long id);






}

