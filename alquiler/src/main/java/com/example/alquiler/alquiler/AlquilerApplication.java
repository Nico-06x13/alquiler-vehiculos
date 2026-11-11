package com.example.alquiler.alquiler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.alquiler.alquiler")
public class AlquilerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlquilerApplication.class, args);
    }
}