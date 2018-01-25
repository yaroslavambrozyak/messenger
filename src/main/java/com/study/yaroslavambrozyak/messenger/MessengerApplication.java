package com.study.yaroslavambrozyak.messenger;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
public class MessengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessengerApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public MessageSource resourceBundleMessageSource(){
		ReloadableResourceBundleMessageSource source= new ReloadableResourceBundleMessageSource();
		source.setBasename("classpath:exception_messages.properties");
		return source;
	}
}
