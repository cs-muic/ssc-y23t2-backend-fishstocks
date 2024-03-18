<<<<<<< HEAD
//package com.example.securingweb.controller;
//
//
//import com.example.securingweb.dto.UserDto;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin("*")
//public class UserController {
//    @PostMapping("/login")
//    public String login(HttpServletRequest request){
//        return "log sucess";
//    }
//    @GetMapping("/logout")
//    public String logout(){
//        return "successfully log out";
//    }
//
////    @PostMapping("/sign")
////    public ResponseBody sign(@RequestBody UserDto userDto){
////        return RequestBody
////    }
//
//    @GetMapping("/register")
//    public String there(){
//        return "log sucess";
//    }
//}
=======
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
>>>>>>> bb6e2f0 (setup basic authentication and recording admin account into database, but not done)
