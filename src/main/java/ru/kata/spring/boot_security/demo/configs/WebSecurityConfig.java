package ru.kata.spring.boot_security.demo.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kata.spring.boot_security.demo.service.UserServiceDetailsImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordConfig passwordConfig;
    private final SuccessUserHandler successUserHandler;
    private final UserServiceDetailsImpl serviceDetails;


    public WebSecurityConfig(PasswordConfig passwordConfig, SuccessUserHandler successUserHandler, UserServiceDetailsImpl serviceDetails) {
        this.passwordConfig = passwordConfig;
        this.successUserHandler = successUserHandler;
        this.serviceDetails = serviceDetails;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()


                .antMatchers("/").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login");

    }


    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordConfig.passwordEncoder());
        provider.setUserDetailsService(serviceDetails);

        auth.authenticationProvider(provider);

        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("$2a$12$vpY721heyk3bVh7xVuiGxuOT9e1ZpsdDS6bo3SMwuw.9yv.FLl22e")
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .roles("ADMIN", "USER")
                .and()
                .withUser("user")
                .password("$2a$12$jwKCSEiMUdUPJqbbomC3HOhAMPUTaI/a7WPjVcgRicR1HamT.gqtO")
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .roles("USER");


    }


}