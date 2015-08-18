package com.etnetera.projects.testreporting.webapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;
import com.mongodb.Mongo;

// TODO uncomment when mongo configuration is defined
/*@Configuration
@EnableMongoAuditing*/
class MongoConfiguration extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mongo mongo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAware<String>() {
			@Override
			public String getCurrentAuditor() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

				if (authentication == null || !authentication.isAuthenticated()) {
					return null;
				}

				return ((User) authentication.getPrincipal()).getId();
			}
		};
	}

}