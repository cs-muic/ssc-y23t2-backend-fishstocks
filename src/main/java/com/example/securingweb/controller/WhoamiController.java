package com.example.securingweb.controller;

import com.example.securingweb.dto.WhoamiDTO;
import com.example.securingweb.repo.UserRepository;
import com.example.securingweb.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WhoamiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/api/whoami")
    public WhoamiDTO whoami(){
        if (userRepository.findByUsername("admin").isEmpty()){
            String encode = encoder.encode("admin");
            userRepository.save(new Customer(0L,"admin",encode));
        }
        WhoamiDTO here;
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Customer u = userRepository.findByUsername(name).get();
            //System.out.println("have3");
            here = WhoamiDTO.builder()
                    .loggedIn(true)
                    .name(u.getUsername())
                    .role(u.getAuthorities())
                    .username(u.getUsername())
                    .build();

        } catch (Exception ignored) {
            here = WhoamiDTO.builder().loggedIn(false).build();
        }
        return here;
    }

}
