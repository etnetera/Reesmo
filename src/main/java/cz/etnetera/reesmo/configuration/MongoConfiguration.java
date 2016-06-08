package cz.etnetera.reesmo.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import cz.etnetera.reesmo.Reesmo;
import cz.etnetera.reesmo.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = Reesmo.PACKAGE + ".repository.mongodb")
@EnableMongoAuditing
class MongoConfiguration extends AbstractMongoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfiguration.class);
	
	@Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private Integer port;

    @Value("${mongodb.dbname}")
    private String dbName;
	
    @Bean
    public GridFsTemplate gridFsTemplate() {
        try {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
        } catch (Exception e) {
            LOGGER.error("Failed to create GridFsTemplate.", e);
        }
        return null;
    }
    
	@Override
	protected String getDatabaseName() {
		return dbName;
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(host, port);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAware<String>() {
			@Override
			public String getCurrentAuditor() {
				UserManager userManager = UserManager.getInstance();
				// user manager can be null on application start
				return userManager == null ? null : userManager.getUserId();
			}
		};
	}

}