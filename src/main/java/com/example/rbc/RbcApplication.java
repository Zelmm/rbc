package com.example.rbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class RbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbcApplication.class, args);
    }

}
