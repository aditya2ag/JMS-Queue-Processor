package com.oracle.jms.queueprocessor;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
@Component
public class SwaggerCustomConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(get_apiInfo()).select()
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
	}

	private ApiInfo get_apiInfo() {
		return new ApiInfo("Oracle JMS Queue processor APIs", "This application API is developed by Oracle", "1.0",
				"https://www.oracle.com/legal/copyright.html",
				new Contact("Oracle", "https://www.oracle.com/corporate/contact/", null), "License of APIs",
				"https://www.oracle.com/legal/terms.html", new ArrayList<>());
	}
	// Copyright 2022 ,Oracle and/or its affiliates. All rights reserved.
}
