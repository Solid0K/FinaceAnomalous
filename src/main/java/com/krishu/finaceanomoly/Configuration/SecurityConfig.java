package com.krishu.finaceanomoly.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtfilter;

    public SecurityConfig(JwtFilter jwtfilter) {
        this.jwtfilter = jwtfilter;
    }

    @Bean
    public BCryptPasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain getFilter(HttpSecurity https){
        https.csrf(AbstractHttpConfigurer::disable);
        https.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        https.authorizeHttpRequests(request->request.requestMatchers("/auth/signup","/auth/signin").permitAll()
                .requestMatchers(HttpMethod.POST,"/expense","/expense/bulk")
                .hasAnyRole("EMPLOYEE","CONTROLLER","ADMIN").requestMatchers(HttpMethod.GET,"/expense/**")
                .hasAnyRole("EMPLOYEE","CONTROLLER","ADMIN").requestMatchers("/expenseReport").hasAnyRole("CONTROLLER","ADMIN")
                .requestMatchers("/logs/**").hasAnyRole("CONTROLLER","ADMIN").
                requestMatchers("/expense/mannualReview/**").hasAnyRole("CONTROLLER","ADMIN").requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated());
        https.addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
        return https.build();
    }
}
