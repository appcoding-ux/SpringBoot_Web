package com.ex00;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Ex00Application {

    public static void main(String[] args) {
        SpringApplication.run(Ex00Application.class, args);
    }

}
