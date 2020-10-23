package com.webApp.config;

import com.webApp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserService();
    }
}
