package com.example.demo.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;


@org.springframework.context.annotation.Configuration
public class ModelMapperConfiguration {


    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }
}

