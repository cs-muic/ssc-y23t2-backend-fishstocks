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
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
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

		http
				.csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection, common in stateless REST APIs.
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(new AntPathRequestMatcher("/user", "POST")).permitAll() // Allow POST requests to /user without authentication
						.anyRequest().authenticated() // Ensures all requests are authenticated.
				)
				.httpBasic(withDefaults()) // Enables HTTP Basic Authentication with default settings.
				.logout(l -> l.deleteCookies("JSESSIONID"));; // Configures session management to be stateless.
		return http.build(); // Builds and returns the SecurityFilterChain.

	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService detailsService){
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(detailsService);
		return new ProviderManager(daoProvider);

	}
}
