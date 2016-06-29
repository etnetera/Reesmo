package cz.etnetera.reesmo.monitor;

import cz.etnetera.reesmo.controller.result.ProjectResultController;
import cz.etnetera.reesmo.controller.result.ResultController;
import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;

import java.util.Date;

public class MonitoringNotification {

    private Monitoring monitoring;

    private String monitoringName;

    private Result result;

    private String resultUri;

    private String viewUri;

    private Date timestamp;

    public MonitoringNotification(Monitoring monitoring, String monitoringName, Result result) {
        this.monitoring = monitoring;
        this.monitoringName = monitoringName;
        if (result != null) {
            this.result = result;
            this.resultUri = ResultController.RESULT_HOME_URI.replaceAll("\\{resultId\\}", result.getId());
            this.timestamp = result.getCreatedAt();
        } else {
            this.timestamp = new Date();
        }
        this.viewUri = ProjectResultController.VIEW_RESULTS_URI.replaceAll("\\{projectId\\}", monitoring.getProjectId())
                .replaceAll("\\{viewId\\}", monitoring.getViewId());
    }

    public Monitoring getMonitoring() {
        return monitoring;
    }

    public Result getResult() {
        return result;
    }

    public String getMonitoringName() {
        return monitoringName;
    }

    public String getResultUri() {
        return resultUri;
    }

    public String getViewUri() {
        return viewUri;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
