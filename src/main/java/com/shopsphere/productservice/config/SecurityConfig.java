package com.shopsphere.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final String[] publicPaths = {
    "/api/products",
    "/api/products/{id}",
    "/api/products/category/{category}",
    "/api/products/summary/bash",
    "/api/products/info/**",
  };

  private final String[] adminPaths = { "/api/products/user/{userId}" };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers(publicPaths)
          .permitAll()
          .requestMatchers(adminPaths)
          .hasRole("ADMIN")
          .anyRequest()
          .authenticated()
      )
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Enable JWT authentication
    return http.build();
  }
}
