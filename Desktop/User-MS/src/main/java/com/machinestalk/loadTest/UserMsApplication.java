package com.machinestalk.loadTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = "com.machinestalk.loadTest")
public class UserMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMsApplication.class, args);
	}

}
