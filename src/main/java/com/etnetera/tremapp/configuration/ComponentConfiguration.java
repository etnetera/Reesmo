package com.etnetera.tremapp.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.etnetera.tremapp.Tremapp;

@Configuration
@ComponentScan(basePackages = Tremapp.PACKAGE)
@Import({SecurityConfiguration.class})
class ComponentConfiguration {
	
}