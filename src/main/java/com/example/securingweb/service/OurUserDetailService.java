package com.example.securingweb.service;

import com.example.securingweb.repo.UserRepository;
import com.example.securingweb.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class OurUserDetailService implements UserDetailsService {
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid user"));
//        if (!username.equals("admin")){
//            throw new RuntimeException("invalid");
//        }else{
//            Customer u = new Customer(1L,"admin",encoder.encode("admin"));
//            userRepository.save(u);
//            return u;
//        }
    }
}