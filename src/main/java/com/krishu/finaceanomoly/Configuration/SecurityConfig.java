package com.krishu.finaceanomoly.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain getFilter(HttpSecurity https){
        https.csrf(AbstractHttpConfigurer::disable);
        https.authorizeHttpRequests(request->request.anyRequest().authenticated());
        return https.build();
    }
}
