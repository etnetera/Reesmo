package cz.etnetera.reesmo.model.datatables.monitor;

import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;

public class MonitorDT {


    private String type;

    private String viewIdDT;

    private boolean enabled;

    private String description;

    private String viewName;

    public MonitorDT(Monitoring monitoring) {
        this.type = monitoring.getClass().getSimpleName();
        this.viewIdDT = monitoring.getViewId();
        this.enabled = monitoring.isEnabled();
        this.description = monitoring.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getViewIdDT() {
        return viewIdDT;
    }

    public void setViewIdDT(String viewIdDT) {
        this.viewIdDT = viewIdDT;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
