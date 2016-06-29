package cz.etnetera.reesmo.monitor.service.monitoring;

import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.monitoring.FlatlineMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.monitor.MonitoringManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

public class FlatlineMonitoringService implements MonitoringService {

    @Autowired
    private MonitoringManager monitoringManager;

    @PostConstruct
    private void init() {
        monitoringManager.registerMonitoringService(this);
    }

    @Override
    public boolean supportsMonitoring(Monitoring monitoring) {
        return monitoring instanceof FlatlineMonitoring;
    }

    @Override
    public boolean shouldNotify(Monitoring monitoring, Result result) {
        return false;
    }

    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    private void refresh() {
        // TODO check for less results in view
    }

}
