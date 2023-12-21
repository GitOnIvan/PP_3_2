package ru.kata.spring.boot_security.demo.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute ("newUser")
    public User newUser() {
        return new User();
    }

    public List<User> getListOfUsers() {
        return userService.findAllUsers();
    }


    @GetMapping("/user")
    public String getUser(Principal principal, Model model) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        model.addAttribute("loggedUser", authToken);


        if (userService.findUserByEmail(principal.getName()) == null) {
            List<User> usersList = new ArrayList<>();
            model.addAttribute("loggedUserDetails", usersList);

        } else {
            List<User> usersList = new ArrayList<>();
            usersList.add(userService.findUserByEmail(principal.getName()));
            model.addAttribute("loggedUserDetails", usersList);
        }

        return "user";
    }



    @GetMapping("/admin")
    public String getUsersList(ModelMap model, Principal principal){

        if (userService.findAll().isEmpty()) {

            userService.addRole((new Role("ROLE_ADMIN")));
            userService.addRole((new Role("ROLE_USER")));
        }


        model.addAttribute("allRoles", userService.findAll());


        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        model.addAttribute("loggedUser", authToken);


        if (userService.findAllUsers() == null) {
            List<User> usersList = new ArrayList<>();
            usersList.add(new User());

            model.addAttribute("loggedUserDetails", usersList);

        } else {
            List<User> usersList = userService.findAllUsers();
            model.addAttribute("loggedUserDetails", usersList);
        }


        return "admin";
    }





    @PostMapping("/admin")
    public String createUser(@ModelAttribute("newUser") User addUser) {
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
    public String updateUser(@PathVariable long userId, @RequestBody String userJson) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson, User.class);

         userService.updateUser(userId, user);

        return "redirect:/admin";
    }





}
