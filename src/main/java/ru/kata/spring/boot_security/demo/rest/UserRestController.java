package ru.kata.spring.boot_security.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@RestController
@RequestMapping("/admin/api/v1/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable long userId) {

        return userService.findUsersById(userId);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody String userJson) {

        System.out.println(userJson + " " + userId);
        // Здесь вы можете добавить логику обновления пользователя в базе данных.

        // Предположим, что вся логика обновления пользователя прошла успешно.
        return ResponseEntity.ok("Пользователь успешно обновлен!");
    }
}
// Другие методы контроллера...
