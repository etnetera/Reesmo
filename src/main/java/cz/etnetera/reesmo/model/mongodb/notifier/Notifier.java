package cz.etnetera.reesmo.model.mongodb.notifier;

import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import org.springframework.data.annotation.Id;

public abstract class Notifier extends MongoAuditedModel {

    @Id
    private String id;

    private String monitorId;

    private boolean enabled;

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
