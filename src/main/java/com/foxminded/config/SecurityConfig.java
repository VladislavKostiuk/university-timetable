package com.foxminded.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers("/adminPanel/**").hasAuthority("ADMIN")
                        .requestMatchers("/timetable").hasAnyAuthority("STUDENT", "TEACHER", "ADMIN")
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
        )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/timetable", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

