package com.example.hishab.hishabApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class HishabAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(HishabAppApplication.class, args);
	}

}
