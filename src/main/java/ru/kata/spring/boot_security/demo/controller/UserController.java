package ru.kata.spring.boot_security.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @ModelAttribute("newUser")
    public User addUser() {
        return new User();
    }




    @GetMapping("/user")
    public String getUser(Principal principal, Model model) {
        User user = userService.findUsersByEmail(principal.getName());
        model.addAttribute("userSolo", user);
        return "user";
    }

    @GetMapping("/admin")
    public String getUsersList(ModelMap model, Principal principal) {
        model.addAttribute("userList", userService.getUsersByEmailNotNull());
        return "admin";
    }


    @PostMapping("/admin")
    public String createUser(@ModelAttribute("newUser") User addUser) {
        userService.addNewUser(addUser);

        return "redirect:/admin";
    }



}
