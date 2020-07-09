package com.example.Ecabinet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EcabinetApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EcabinetApplication.class, args);
	}

}
