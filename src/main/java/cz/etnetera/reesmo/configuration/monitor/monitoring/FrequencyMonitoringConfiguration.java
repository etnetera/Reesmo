package cz.etnetera.reesmo.configuration.monitor.monitoring;

import cz.etnetera.reesmo.monitor.service.monitoring.FrequencyMonitoringService;
import org.springframework.context.annotation.Configuration;

@Configuration
class FrequencyMonitoringConfiguration {

    //@Bean
    public FrequencyMonitoringService frequencyMonitoringService() {
        return new FrequencyMonitoringService();
    }

}