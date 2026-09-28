package com.shopsphere.productservice.config;

import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final String[] publicPaths = {
    "/api/products",
    "/api/products/{id}",
    "/api/products/category/{category}",
  };

  private final String[] servicePaths = {
    "/api/products/summary/bash",
    "/api/products/info/**",
  };

  private final String[] adminPaths = { "/api/products/user/{userId}" };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers(publicPaths)
          .permitAll()
          .requestMatchers(servicePaths)
          .hasAuthority("ROLE_SERVICE")
          .requestMatchers(adminPaths)
          .hasRole("ADMIN")
          .anyRequest()
          .authenticated()
      )
      .oauth2ResourceServer(oauth2 ->
        oauth2.jwt(jwt ->
          jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
        )
      );
    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwtRoleConverter());
    return converter;
  }

  /**
   * Extracts the 'role' claim from the JWT and maps it to
   * a ROLE_ prefixed GrantedAuthority.
   */
  private Converter<Jwt, Collection<GrantedAuthority>> jwtRoleConverter() {
    return jwt -> {
      String role = jwt.getClaimAsString("role");
      if (role == null || role.isBlank()) {
        return List.of();
      }
      return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    };
  }
}
