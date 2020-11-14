package com.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages={"com.webapp"})
@ConfigurationPropertiesScan
public class MainConfig {
    public static void main(String[] args) {
        SpringApplication.run(MainConfig.class, args);
    }
}
