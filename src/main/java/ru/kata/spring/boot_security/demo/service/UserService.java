package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;

    private UserRepo userRepo;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUsersByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User with '%s' not found", email));
        }
        return user;
    }


    @Transactional
    public User findUsersByEmail(String email) {
        return userRepo.findUsersByEmail(email);
    }


    @Transactional
    public User findUsersById (Long id) {
        return userRepo.findUsersById(id);
    }

    @Transactional
    public User findUsersByIdExtended (Long id){
        User userToBeUpdate = findUsersById(id);
        User modelUser = new User();

        modelUser.setFirstName(userToBeUpdate.getFirstName());
        modelUser.setLastName(userToBeUpdate.getLastName());
        modelUser.setAge(userToBeUpdate.getAge());
        modelUser.setSex(userToBeUpdate.getSex());
        modelUser.setEmail(userToBeUpdate.getEmail());
        modelUser.setPass("");
        modelUser.setRole(userToBeUpdate.getRole());
        modelUser.setStatus(userToBeUpdate.getStatus());

        return modelUser;

    }




    @Transactional
    public List<User> findAll(){
        return userRepo.findAll();
    }



    @Transactional
    public boolean addNewUser (User user) {
        User userDB = userRepo.findUsersByEmail(user.getEmail());
        if (userDB != null)
            return false;

        user.setPass(passwordEncoder.encode(user.getPass()));
        em.persist(user);
        return true;

    }
    @Transactional
    public void updateUser(long id, User updatedUser) {
        User userToBeUpdate = findUsersById(id);

        userToBeUpdate.setFirstName(updatedUser.getFirstName());
        userToBeUpdate.setLastName(updatedUser.getLastName());
        userToBeUpdate.setAge(updatedUser.getAge());
        userToBeUpdate.setSex(updatedUser.getSex());
        userToBeUpdate.setEmail(updatedUser.getEmail());
        userToBeUpdate.setPass(passwordEncoder.encode(updatedUser.getPass()));
        userToBeUpdate.setRole(updatedUser.getRole());
        userToBeUpdate.setStatus(updatedUser.getStatus());


    }

}
