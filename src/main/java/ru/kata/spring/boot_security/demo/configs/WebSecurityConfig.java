package ru.kata.spring.boot_security.demo.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kata.spring.boot_security.demo.service.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordConfig passwordConfig;
    private final SuccessUserHandler successUserHandler;
    private final UserService userService;

    @Autowired
    public WebSecurityConfig(PasswordConfig passwordConfig, SuccessUserHandler successUserHandler, UserService userService) {
        this.passwordConfig = passwordConfig;
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()

                //Изменить его потом на "/"
                .antMatchers("/", "/index").permitAll()

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

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordConfig.passwordEncoder());
//        provider.setUserDetailsService(userService);
//
//        return provider;
//    }
//
//    @Autowired
//    public void configureInMemoryAuthentication(AuthenticationManagerBuilder auth) throws Exception
//    {
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password("$2a$12$vpY721heyk3bVh7xVuiGxuOT9e1ZpsdDS6bo3SMwuw.9yv.FLl22e")
//                .roles("ADMIN");
//
//
//    }

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordConfig.passwordEncoder());
        provider.setUserDetailsService(userService);

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