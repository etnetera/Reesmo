package cz.etnetera.reesmo.monitor.service.monitoring;

import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;

public interface MonitoringService {

    /**
     * Checks if passed monitoring instance is supported
     * by this service.
     *
     * @param monitoring Monitoring instance
     * @return true if given monitoring is supported by this service
     */
    boolean supportsMonitoring(Monitoring monitoring);

    /**
     * Resolve result according given monitoring and returns
     * true if result should be notified.
     *
     * @param monitoring Monitoring instance
     * @param result Result result
     * @return true if result should be notified
     */
    boolean shouldNotify(Monitoring monitoring, Result result);

}
