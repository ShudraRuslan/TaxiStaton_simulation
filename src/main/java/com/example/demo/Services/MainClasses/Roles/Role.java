package com.example.demo.Services.MainClasses.Roles;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    SUPERUSER;

    @Override
    public String getAuthority() {
        return name();
    }
}
