package org.mai.roombooking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> ips = Arrays.asList(
                "localhost",
                "192.168.1.8",
                "10.10.67.118",
                "10.10.71.185",
                "10.9.8.193");

        ips = ips.stream().map(element -> "http://" + element + ":3000").toList();

        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000", "http://192.168.1.8:3000", "http://10.10.67.118:3000", "http://10.10.71.185:3000")
                .allowedOrigins(ips.toArray(new String[0]))
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type", "Access-Control-Allow-Origin");
    }
}