package cz.etnetera.reesmo.monitor.service.monitoring;

import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.monitor.MonitoringManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class FrequencyMonitoringService implements MonitoringService {

    @Autowired
    private MonitoringManager monitoringManager;

    @PostConstruct
    private void init() {
        monitoringManager.registerMonitoringService(this);
    }

    @Override
    public boolean supportsMonitoring(Monitoring monitoring) {
        return monitoring instanceof FrequencyMonitoring;
    }

    @Override
    public boolean shouldNotify(Monitoring monitoring, Result result) {
        return true;
    }

}
