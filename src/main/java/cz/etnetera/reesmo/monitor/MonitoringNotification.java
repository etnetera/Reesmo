package cz.etnetera.reesmo.monitor;

import cz.etnetera.reesmo.controller.result.ProjectResultController;
import cz.etnetera.reesmo.controller.result.ResultController;
import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.view.View;

import java.util.Date;

public class MonitoringNotification {

    private Monitoring monitoring;

    private String monitoringName;

    private View view;

    private String viewUri;

    private Result result;

    private String resultUri;

    private Date timestamp;

    public MonitoringNotification(Monitoring monitoring, String monitoringName, View view, Result result) {
        this.monitoring = monitoring;
        this.monitoringName = monitoringName;
        this.view = view;
        this.viewUri = ProjectResultController.VIEW_RESULTS_URI.replaceAll("\\{projectId\\}", monitoring.getProjectId())
                .replaceAll("\\{viewId\\}", monitoring.getViewId());
        if (result != null) {
            this.result = result;
            this.resultUri = ResultController.RESULT_HOME_URI.replaceAll("\\{resultId\\}", result.getId());
            this.timestamp = result.getCreatedAt();
        } else {
            this.timestamp = new Date();
        }
    }

    public Monitoring getMonitoring() {
        return monitoring;
    }

    public String getMonitoringName() {
        return monitoringName;
    }

    public View getView() {
        return view;
    }

    public String getViewUri() {
        return viewUri;
    }

    public Result getResult() {
        return result;
    }

    public String getResultUri() {
        return resultUri;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
