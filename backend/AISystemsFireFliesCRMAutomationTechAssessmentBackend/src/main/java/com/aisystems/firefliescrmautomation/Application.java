package com.aisystems.firefliescrmautomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

/**
 * Main Application Class
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 5 December 2025 4:36 AM GMT -5 Bogotá DC Colombia
 * @param args
 */ 
@OpenAPIDefinition(
		info = @Info(
				title = "IUVENTISHEALTH API",
				version = "1.0",
				description = "This is a custom Swagger UI for My API",
				contact = @Contact(name = "Manuela Cortés Granados", email = "manuela@example.com"),
				license = @License(name = "Apache 2.0", url = "http://springdoc.org")
		)
)
@SpringBootApplication
public class Application {

    public Application() {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
