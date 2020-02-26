package com.davidagood.jackson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {Metadata.class})
public class JacksonWrappingUnwrappingApplication {

    public static void main(String[] args) {
        SpringApplication.run(JacksonWrappingUnwrappingApplication.class, args);
    }

}
