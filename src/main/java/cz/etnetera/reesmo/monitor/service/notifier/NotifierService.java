package cz.etnetera.reesmo.monitor.service.notifier;

import cz.etnetera.reesmo.model.mongodb.notifier.Notifier;
import cz.etnetera.reesmo.monitor.MonitoringNotification;

public interface NotifierService {

    /**
     * Checks if passed notifier instance is supported
     * by this service.
     *
     * @param notifier Notifier instance
     * @return true if given notifier is supported by this service
     */
    boolean supportsNotifier(Notifier notifier);

    /**
     * Sends monitoring notification.
     *
     * @param notifier Notifier instance
     * @param notification monitoring notification data
     */
    void notifyMonitoring(Notifier notifier, MonitoringNotification notification);

}
