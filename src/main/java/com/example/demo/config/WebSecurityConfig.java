//package com.example.demo.config;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//import org.springframework.security.web.authentication.RememberMeServices;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    @Autowired
//    private ConfigurableBeanFactory beanFactory;
//
//    @Bean
//    public static PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        var chain = http
//                .authorizeHttpRequests(customizer -> customizer
//                        .requestMatchers("/api/csrf").permitAll()
//                        .requestMatchers("/api/login").permitAll()
//                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().denyAll())
//                .exceptionHandling(customizer -> customizer
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
//                .rememberMe(customizer -> customizer.alwaysRemember(true).key("demo"))
//                .build();
//
//        var rememberMeServices = http.getSharedObject(RememberMeServices.class);
//        beanFactory.registerSingleton("rememberMeServices", rememberMeServices);
//        return chain;
//    }
//}

