package com.example.securingweb.service;


import com.example.securingweb.repo.UserRepository;
import com.example.securingweb.user.Customer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public Customer registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()){
            String encode = encoder.encode(password);
            return userRepository.save(new Customer(0L,username,encode));
        }else{
            throw new RuntimeException("already exist");
        }
    }


}
