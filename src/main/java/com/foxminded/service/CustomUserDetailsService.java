package com.foxminded.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {
    boolean isNameAvailable(String newName);
}
