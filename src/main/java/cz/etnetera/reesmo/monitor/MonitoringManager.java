package cz.etnetera.reesmo.monitor;

import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.notifier.Notifier;
import cz.etnetera.reesmo.model.mongodb.resultchange.ResultChange;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.monitor.service.monitoring.MonitoringService;
import cz.etnetera.reesmo.monitor.service.notifier.NotifierService;
import cz.etnetera.reesmo.repository.elasticsearch.result.ResultRepository;
import cz.etnetera.reesmo.repository.mongodb.monitor.MonitorRepository;
import cz.etnetera.reesmo.repository.mongodb.notifier.NotifierRepository;
import cz.etnetera.reesmo.repository.mongodb.resultchange.ResultChangeRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonitoringManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringManager.class);

    @Autowired
    private ResultChangeRepository resultChangeRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private NotifierRepository notifierRepository;

    private List<MonitoringService> monitoringServices = new ArrayList<>();

    private List<NotifierService> notifierServices = new ArrayList<>();

    public void registerMonitoringService(MonitoringService monitoringService) {
        monitoringServices.add(monitoringService);
    }

    public void registerNotifierService(NotifierService notifierService) {
        notifierServices.add(notifierService);
    }

    public void notifyMonitoring(MonitoringService monitoringService, Monitoring monitoring) {
        notifyMonitoring(monitoringService, monitoring, null);
    }

    public void notifyMonitoring(MonitoringService monitoringService, Monitoring monitoring, Result result) {
        View view = viewRepository.findOne(monitoring.getViewId());
        MonitoringNotification notification = new MonitoringNotification(monitoring, view.getName() + " " + monitoring.toString(), view, result);
        notifierRepository.findAllByMonitoring(monitoring.getId())
                .stream()
                .filter(Notifier::isEnabled)
                .forEach(n -> {
                    notifierServices.stream().filter(ns -> ns.supportsNotifier(n)).forEach(ns -> {
                        ns.notifyMonitoring(n, notification);
                    });
                });
    }

    @Scheduled(fixedRate = 5000, initialDelay = 10000)
    private void refresh() {
        Date startDate = new Date();
        LOGGER.debug("Result changes refresh started");
        List<ResultChange> changes = resultChangeRepository.findAll();
        changes.stream().forEach(change -> {
            boolean resolved = true;
            Result result = null;
            try {
                result = resultRepository.findOne(change.getResultId());
            } catch (Exception e) {
                resolved = false;
                LOGGER.error("Unable to load result for change " + change, e);
            }
            if (result != null) {
                try {
                    resolveChange(result);
                } catch (Exception e) {
                    resolved = false;
                    LOGGER.error("Unable to resolve change " + change, e);
                }
            }
            if (resolved)
                resultChangeRepository.delete(change);
        });
        LOGGER.debug("Result changes refresh finished in " + (new Date().getTime() - startDate.getTime()) + " ms");
    }

    private void resolveChange(Result result) {
        monitorRepository.findMonitorsForProject(result.getProjectId())
            .stream()
            .filter(m -> m.isEnabled() && resultRepository.isResultInView(m.getViewId(), result.getId()))
            .forEach(m -> {
                monitoringServices.stream().filter(ms -> ms.supportsMonitoring(m)).forEach(ms -> {
                    if (ms.shouldNotify(m, result)) {
                        notifyMonitoring(ms, m, result);
                    }
                });
            });
    }

}
