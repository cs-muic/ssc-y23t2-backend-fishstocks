package com.example.securingweb;

import com.example.securingweb.repo.UserRepository;
import com.example.securingweb.user.Customer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SecuringWebApplication {

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(SecuringWebApplication.class, args);
	}

//	@Bean
//	CommandLineRunner run(UserRepository userRepository, PasswordEncoder encoder){
//		return args -> {
//			Customer user = new Customer(1L,"admin", encoder.encode("admin"));
//			userRepository.save(user);
//		};
//	}

}
