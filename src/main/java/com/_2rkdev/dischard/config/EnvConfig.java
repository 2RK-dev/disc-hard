package com._2rkdev.dischard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class EnvConfig {

    @Bean
    public Properties dotenvProperties(ConfigurableEnvironment environment) throws IOException {
        Properties properties = new Properties();
        properties.load(new ClassPathResource(".env").getInputStream());

        properties.forEach((key, value) ->
                System.setProperty(key.toString(), value.toString()));

        return properties;
    }
}
