package com.educative.packageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;

@SpringBootApplication
public class PackageserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PackageserviceApplication.class, args);
	}
	
	@Bean
	public JacksonPubSubMessageConverter jacksonPubSubMessageConverter(ObjectMapper objectMapper) {
	 return new JacksonPubSubMessageConverter(objectMapper);
	}

}
