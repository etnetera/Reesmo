package cz.etnetera.reesmo.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cz.etnetera.reesmo.Reesmo;

@Configuration
@ComponentScan(basePackages = { Reesmo.PACKAGE, "com.github.dandelion.datatables.service", "com.github.dandelion.datatables.repository" })
@Import({SecurityConfiguration.class})
class ComponentConfiguration {
	
}