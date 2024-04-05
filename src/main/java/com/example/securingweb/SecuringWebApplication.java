package com.example.securingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecuringWebApplication {
	//13306
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
