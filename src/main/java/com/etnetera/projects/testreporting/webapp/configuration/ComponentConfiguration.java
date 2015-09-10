package com.etnetera.projects.testreporting.webapp.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.etnetera.projects.testreporting.webapp")
@Import({SecurityConfiguration.class})
class ComponentConfiguration {
	
}