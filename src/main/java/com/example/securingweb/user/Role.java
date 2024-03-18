package com.example.securingweb.user;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;



@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return "USER";
    }
}
