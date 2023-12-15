package com.makarov.laba3_v14.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    PUBLISHER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
