package ru.kata.spring.boot_security.demo.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.List;



@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("newUser")
    public User newUser() {
        return new User();
    }



    @GetMapping("/user")
    public String getUser(Principal principal, Model model) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        model.addAttribute("loggedUser", authToken);

        return "user";
    }




    @GetMapping("/admin")
    public String getUsersList(ModelMap model, Principal principal) {

        if (userService.findAll().isEmpty()) {

            userService.addRole((new Role("ROLE_ADMIN")));
            userService.addRole((new Role("ROLE_USER")));
        }

        model.addAttribute("allRoles", userService.findAll());

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        model.addAttribute("loggedUser", authToken);

        return "admin";
    }















    public List<User> getListOfUsers() {
        return userService.findAllUsers();
    }



    @PostMapping("/admin")
    public String createUser(@Validated @ModelAttribute("newUser") User addUser,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            return "redirect:/admin";
        }
        User userAdd = getListOfUsers().stream()
                .filter(user -> user.getEmail().equals(addUser.getEmail()))
                .findAny().orElse(null);

        if (userAdd == null) {
            userService.addNewUser(addUser);
        }
        return "redirect:/admin";



    }


    @PostMapping("/admin/delete/{userId}")
    public String deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);

        return "redirect:/admin";
    }


    @PostMapping("/admin/edit/{userId}")
    public String updateUser(@PathVariable long userId, @Validated @RequestBody String userJson,
                             BindingResult bindingResult){

        if (bindingResult.hasErrors()) {

            return "redirect:/admin";
        }

        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        try {
            user = objectMapper.readValue(userJson, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        userService.updateUser(userId, user);

        return "redirect:/admin";
    }


}
