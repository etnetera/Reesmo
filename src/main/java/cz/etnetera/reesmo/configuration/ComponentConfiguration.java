package cz.etnetera.reesmo.configuration;

import cz.etnetera.reesmo.Reesmo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = { Reesmo.PACKAGE, "com.github.dandelion.datatables.service", "com.github.dandelion.datatables.repository" })
@Import({SecurityConfiguration.class})
class ComponentConfiguration {
	
}