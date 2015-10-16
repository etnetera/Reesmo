package com.etnetera.tremapp.web.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.etnetera.tremapp.web")
@Import({SecurityConfiguration.class})
class ComponentConfiguration {
	
}