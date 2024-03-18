package com.example.securingweb.user;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Builder
@Data
@Entity
@Table(name = "app_user")
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;


    @Column(nullable = false)
    private String password;

    public Customer(Long id, String username, String password){
        this.password = password;
        this.id = id;
        this.username = username;
    }
    public Customer() {
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> s = new HashSet<>();
        s.add(new Role());
        return s;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
