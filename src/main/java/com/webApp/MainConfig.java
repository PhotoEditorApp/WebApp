package com.webApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.webApp"})
public class MainConfig {
    public static void main(String[] args) {
        SpringApplication.run(MainConfig.class, args);
    }
}
