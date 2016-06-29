package cz.etnetera.reesmo.configuration.monitor.monitoring;

import cz.etnetera.reesmo.monitor.service.monitoring.FlatlineMonitoringService;
import org.springframework.context.annotation.Configuration;

@Configuration
class FlatlineMonitoringConfiguration {

    //@Bean
    public FlatlineMonitoringService flatlineMonitoringService() {
        return new FlatlineMonitoringService();
    }

}