package com.hope.projectrepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.hope.projectrepository")
@SpringBootApplication
public class ProjectRepositoryApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjectRepositoryApplication.class, args);
	}
}
