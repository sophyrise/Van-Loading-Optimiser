package com.vanopt.vanopt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VanoptApplication {

    public static void main(String[] args) {
        SpringApplication.run(VanoptApplication.class, args);
        System.out.println("  Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("  API docs:   http://localhost:8080/v3/api-docs");
    }
}
