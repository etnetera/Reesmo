package cz.etnetera.reesmo.configuration;

import cz.etnetera.reesmo.monitor.MonitoringManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MonitoringConfiguration {

    @Bean
    public MonitoringManager monitoringManager() {
        return new MonitoringManager();
    }

}