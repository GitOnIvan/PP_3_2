package ru.kata.spring.boot_security.demo.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;




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








// Delete this method after fixes !!!






    @GetMapping("/")
    public String getUsersListTest(ModelMap model, Principal principal) {


        if (getListOfUsers() == null) {
            List<User> users = new ArrayList<>();
            model.addAttribute("loggedUserDetails", users);
        } else {
            model.addAttribute("loggedUserDetails", getListOfUsers());
        }




        model.addAttribute("test",getListOfUsers().stream()
                .filter(user -> user.getEmail().equals("workarthuron@gmail.com"))
                .findAny().orElse(null));


        return "index";
    }





}
