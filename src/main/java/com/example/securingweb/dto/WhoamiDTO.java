package com.example.securingweb.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
@Setter
@Getter
public class WhoamiDTO {
    private boolean loggedIn = false;
    private String username;

    private String name;
    private Collection<? extends GrantedAuthority> role;
}
