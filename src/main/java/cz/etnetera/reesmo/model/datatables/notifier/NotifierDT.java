package cz.etnetera.reesmo.model.datatables.notifier;

import cz.etnetera.reesmo.notifier.EmailNotifier;
import cz.etnetera.reesmo.notifier.Notifier;
import cz.etnetera.reesmo.notifier.URLNotifier;

import java.util.stream.Collectors;

public class NotifierDT {

    private String id;

    private boolean enabled;

    private String type;

    private String addresses;

    private String monitorId;

    public NotifierDT(Notifier notifier) {
        this.id = notifier.getId();
        this.enabled = notifier.isEnabled();
        this.type = notifier.getClass().getSimpleName();
        this.monitorId = notifier.getMonitorId();
        if (notifier instanceof EmailNotifier){
            EmailNotifier emailNotifier = (EmailNotifier) notifier;
            this.addresses = emailNotifier.getAddresses().stream().collect(Collectors.joining(System.lineSeparator()));
        }
        if (notifier instanceof URLNotifier){
            URLNotifier urlNotifier = (URLNotifier) notifier;
            this.addresses = urlNotifier.getAddresses().stream().collect(Collectors.joining(System.lineSeparator()));
        }
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

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

}
