package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Transactional
    public User findUsersByEmail(String email) {
        return userRepo.findUsersByEmail(email);
    }

    @Transactional
    public List<User> getUsersByEmailNotNull(){
        return userRepo.getUsersByEmailNotNull();
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
    public void addNewUser (User user) {
       em.persist(user);

    }



}
