package com.example.securingweb.controller;


import com.example.securingweb.dto.UserDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @GetMapping("/")
    public String here(){
        return "log sucess";
    }
    @GetMapping("/logout")
    public String logout(){
        return "successfully log out";
    }

//    @PostMapping("/sign")
//    public ResponseBody sign(@RequestBody UserDto userDto){
//        return RequestBody
//    }

    @GetMapping("/register")
    public String there(){
        return "log sucess";
    }
}
