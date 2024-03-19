package com.example.securingweb.controller;


import com.example.securingweb.dto.SimpleResponseDTO;
import com.example.securingweb.dto.UserDto;
import com.example.securingweb.service.AuthService;
import com.example.securingweb.user.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AuthController {

    @PostMapping("/login")
    public SimpleResponseDTO login(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try{
            // very bad, unreadable code, but Im following ajarn
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && principal instanceof User){
                request.logout();
            }
            request.login(username,password);
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

    }
    @GetMapping("/logout")
    public SimpleResponseDTO logout(HttpServletRequest request){
        try{
            request.logout();
            return SimpleResponseDTO.builder()
                    .success(true)
                    .message("you are log out successfully")
                    .build();
        } catch (ServletException e) {
            return SimpleResponseDTO.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }

    }

//    private AuthService authService;
//

//    @PostMapping("/register")
//    public Customer registerUser(@RequestBody UserDto body){
//        return authService.registerUser(body.getUsername(), body.getPassword());
//    }


}
