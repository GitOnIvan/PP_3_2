package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    USER;

    private User user;

    @Override
    public String getAuthority() {
        return user.getRole().name();
    }
}
