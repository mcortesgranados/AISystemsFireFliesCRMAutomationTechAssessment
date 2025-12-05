package com.aisystems.firefliescrmautomation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Ensures Swagger UI webjar assets are served from versionless paths.
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 5 December 2025 4:50 AM GMT -5 Bogotá DC Colombia
 */
@Configuration
public class SwaggerUiConfig implements WebMvcConfigurer {

    /**
     * Adds resource handlers to serve Swagger UI static assets from the webjars location.
     * This ensures that requests to /swagger-ui/** are mapped to the correct resources.
     * @since 5 December 2025 8:27 AM GMT -5 Bogotá DC Colombia
     * @param registry the ResourceHandlerRegistry to configure
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    /**
     * Adds view controllers to forward /swagger-ui/ and /swagger-ui.html to the Swagger UI index page.
     * This allows accessing Swagger UI from both paths.
     *
     * @param registry the ViewControllerRegistry to configure
     * @since 5 December 2025 8:30 AM GMT -5 Bogotá DC Colombia
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * 
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:/swagger-ui/index.html");
        registry.addViewController("/swagger-ui.html")
                .setViewName("forward:/swagger-ui/index.html");
    }
}
