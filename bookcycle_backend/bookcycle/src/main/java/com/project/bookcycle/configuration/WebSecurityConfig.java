package com.project.bookcycle.configuration;

import com.project.bookcycle.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(String.format("%s/users/register", apiPrefix),
                                String.format("%s/users/login", apiPrefix)).permitAll()
                        .requestMatchers(GET,
                                String.format("%s/products**", apiPrefix)).permitAll()
                        .requestMatchers(GET,
                                String.format("%s/products/images/*", apiPrefix)).permitAll()
                        .requestMatchers(POST,
                                String.format("%s/products", apiPrefix)).hasRole("USER")
                        .requestMatchers(POST,
                                String.format("%s/products/byuser/{id}", apiPrefix)).hasRole("USER")
                        .requestMatchers(POST,
                                String.format("%s/products/bynotuser/{id}", apiPrefix)).hasRole("USER")
                        .requestMatchers(DELETE,
                                String.format("%s/products/{id}", apiPrefix)).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(PUT,
                                String.format("%s/products/{id}", apiPrefix)).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(POST,
                                String.format("%s/products/get_list", apiPrefix)).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(DELETE,
                                String.format("%s/products/thumbnails/{id}", apiPrefix)).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(GET,
                                String.format("%s/productimages/thumbnails/*", apiPrefix)).permitAll()
                        .requestMatchers(DELETE,
                                String.format("%s/productimages/thumbnails/*", apiPrefix)).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(POST,
                                String.format("%s/orders", apiPrefix)).hasRole("USER")
                        .requestMatchers(GET,
                                String.format("%s/orders/booksaving", apiPrefix)).hasRole("USER")
                        .requestMatchers(POST,
                                String.format("%s/users**", apiPrefix)).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(PUT,
                                String.format("%s/users/{id}", apiPrefix)).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(GET,
                                String.format("%s/users/images/*", apiPrefix)).permitAll()
                        .requestMatchers(POST,
                                String.format("%s/users/{id}", apiPrefix)).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(POST,
                                String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                        .requestMatchers(GET,
                                String.format("%s/categories**", apiPrefix)).permitAll()
                        .requestMatchers(GET,
                                String.format("%s/categories/*", apiPrefix)).permitAll()
                        .requestMatchers(GET,
                                "/chat").permitAll()
                        .anyRequest().authenticated());

        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });

        return httpSecurity.build();
    }

}
