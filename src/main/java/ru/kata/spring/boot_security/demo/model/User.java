package ru.kata.spring.boot_security.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Email(message = "Wrong email!")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Password should not be empty!")
    @Column(name = "password")
    private String pass;

    @NotEmpty(message = "Name should not be empty!")
    @Pattern(regexp = "^[^0-9]*$", message = "Name should not contain digits.")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last Name should not be empty!")
    @Pattern(regexp = "^[^0-9]*$", message = "Last Name should not contain digits.")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "The 'Age' should not be empty!")
    @PositiveOrZero(message = "The 'Age' should be between 0 and 200")
    @Max(value=200, message="The 'Age' should be between 0 and 200")
    @Column(name = "age")
    private Integer age;

    @Pattern(regexp = "^[FM]$", message = "The field should be 'F'(Female) or 'M'(Male).")
    @Column(name = "gender")
    private String gender;


    @ManyToMany
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole();
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status.equals(Status.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return status.equals(Status.ACTIVE);
    }

    public User(String email, String pass, String firstName, String lastName, Integer age, String gender, Set<Role> role, Status status) {
        this.email = email;
        this.pass = pass;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.status = status;
    }

    public User(String email, String pass, String firstName, String lastName, Integer age, String gender, Status status) {
        this.email = email;
        this.pass = pass;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.status = status;
    }


}
