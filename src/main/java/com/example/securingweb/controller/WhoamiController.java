package com.example.securingweb.controller;

import com.example.securingweb.dto.SimpleResponseDTO;
import com.example.securingweb.dto.WhoamiDTO;
import com.example.securingweb.repo.UserRepository;
import com.example.securingweb.user.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/api/register")
    public SimpleResponseDTO register(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userRepository.findByUsername(username).isEmpty()) {
            String encode = encoder.encode(password);
            userRepository.save(new Customer(0L, username, encode));
            try {
                //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                request.login(username, password);
                return SimpleResponseDTO.builder()
                        .success(true)
                        .message("you are log in successfully")
                        .build();
            } catch (ServletException e) {
                return SimpleResponseDTO.builder()
                        .success(false)
                        .message(e.getMessage())
                        .build();
            }
        }else{
            return SimpleResponseDTO.builder()
                    .success(false)
                    .message("already exist")
                    .build();
        }

    }

}
