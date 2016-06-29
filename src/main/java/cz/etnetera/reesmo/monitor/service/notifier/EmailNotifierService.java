package cz.etnetera.reesmo.monitor.service.notifier;

import cz.etnetera.reesmo.model.mongodb.notifier.EmailNotifier;
import cz.etnetera.reesmo.model.mongodb.notifier.Notifier;
import cz.etnetera.reesmo.monitor.MonitoringManager;
import cz.etnetera.reesmo.monitor.MonitoringNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class EmailNotifierService implements NotifierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotifierService.class);

    @Autowired
    private MonitoringManager monitoringManager;

    @PostConstruct
    private void init() {
        monitoringManager.registerNotifierService(this);
    }


    @Override
    public boolean supportsNotifier(Notifier notifier) {
        return notifier instanceof EmailNotifier;
    }

    @Override
    public void notifyMonitoring(Notifier notifier, MonitoringNotification notification) {
        EmailNotifier emailNotifier = (EmailNotifier) notifier;
        emailNotifier.getAddresses().forEach(email -> {
            sendNotification(emailNotifier, notification, email);
        });
    }

    private void sendNotification(EmailNotifier notifier, MonitoringNotification notification, String email) {
        // TODO
        LOGGER.info("Email notification sent to email " + email);
    }

}
