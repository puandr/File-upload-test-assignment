package com.example.file_upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        //TODO remove h2
                        //TODO remove all unnecessary opened
                        .requestMatchers("/files/upload").permitAll()
                        .requestMatchers("/ping").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/files/uploaded-by").permitAll()
                        .anyRequest().authenticated()
                )
                //TODO remove frames, needed only for H2 console
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


}
