package cz.etnetera.reesmo.configuration.monitor.monitoring;

import cz.etnetera.reesmo.monitor.service.monitoring.AnyMonitoringService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AnyMonitoringConfiguration {

    @Bean
    public AnyMonitoringService anyMonitoringService() {
        return new AnyMonitoringService();
    }

}