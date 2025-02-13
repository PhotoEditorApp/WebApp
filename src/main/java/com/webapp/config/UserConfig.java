package com.webapp.config;

import com.webapp.domain.Space;
import com.webapp.service.UserAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserAccountService();
    }

}
