package com.example.hishab.hishabApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


/*netstat -ano | findstr :8080
taskkill /PID <PID> /F*/
@EnableWebMvc
@SpringBootApplication
public class HishabAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(HishabAppApplication.class, args);
	}

}