package com.foxminded.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    STUDENT,
    TEACHER;

    @Override
    public String getAuthority() {
        return name();
    }
}
