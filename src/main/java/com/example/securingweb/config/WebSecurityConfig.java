package com.example.securingweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:8080");
			}
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(AbstractHttpConfigurer::disable)
//			.authorizeHttpRequests((requests) -> requests
//				.requestMatchers("/", "/user").permitAll()
//				.anyRequest().authenticated()).httpBasic(Customizer.withDefaults());

//			.formLogin((form) -> form
//				.loginPage("/login")
//				.permitAll()
//			)
//			.logout((logout) -> logout.permitAll());


		//new one
//		http
//				.csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection, common in stateless REST APIs.
//				.authorizeHttpRequests(authorize -> {authorize
//						.requestMatchers(new AntPathRequestMatcher("/user", "POST")).permitAll() // Allow POST requests to /user without authentication
//						.requestMatchers( new AntPathRequestMatcher("/**")).permitAll()
//								.anyRequest().authenticated();// Ensures all requests are authenticated.
//				}
//				)
//				.httpBasic(withDefaults()) // Enables HTTP Basic Authentication with default settings.
//				.logout(l -> l.deleteCookies("JSESSIONID"));; // Configures session management to be stateless.

//		http.csrf(AbstractHttpConfigurer::disable)
//				.authorizeHttpRequests( auth -> {
//					auth.requestMatchers("/","/api/login","/api/logout","/api/whoami").permitAll();
//					auth.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll();
//				});
		http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests( req -> req.anyRequest().permitAll()).httpBasic(Customizer.withDefaults());

//		http.exceptionHandling(auth -> auth.authenticationEntryPoint(new JsonFobiddenEntryPoint()));
//		http.authorizeHttpRequests( auth -> auth.requestMatchers("/**").permitAll());
		return http.build(); // Builds and returns the SecurityFilterChain.

	}


	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService detailsService){
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(detailsService);
		return new ProviderManager(daoProvider);

	}
}