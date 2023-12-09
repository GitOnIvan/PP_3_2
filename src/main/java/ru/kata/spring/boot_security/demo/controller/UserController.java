package ru.kata.spring.boot_security.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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
    public String getUsersList(@RequestParam(value = "id", required = false, defaultValue = "1") Long id, ModelMap model) {
        model.addAttribute("userList", userService.findAll());
        model.addAttribute("userUpdate", userService.findUsersById(id));

        return "admin";
    }


    @PostMapping(value = "/admin", params = { "id" })
    public String upDate(@ModelAttribute("userUpdate") User updateUser,
                         @RequestParam(value = "id", required = false) Long id) {


        userService.updateUser(id, updateUser);
        return "redirect:/admin";

    }


    @PostMapping("/admin")
    public String createUser(@ModelAttribute("newUser") User addUser) {

        userService.addNewUser(addUser);


        return "redirect:/admin";
    }


    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) {
        userService.deleteById(id);

        return "redirect:/admin";
    }



}
