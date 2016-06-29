package cz.etnetera.reesmo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * As default application.properties in classpath are used.
 * Optional environment specific properties can be specified as system property like:
 * 	-Denvironment.properties.path="/srv/webapps/reesmo/environment.properties"
 */
@Configuration
@PropertySource("classpath:application.properties")
@PropertySource(value = "file:${environment.properties.path}", ignoreResourceNotFound = true)
public class PropertiesConfiguration {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
