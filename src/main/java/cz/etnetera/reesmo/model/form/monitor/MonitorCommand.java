package cz.etnetera.reesmo.model.form.monitor;

import cz.etnetera.reesmo.model.mongodb.monitoring.FlatlineMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.util.concurrent.TimeUnit;

public class MonitorCommand {

    private String id;

    private String type;

    private TimeUnit timeUnit;

    @Min(value = 1, message = "Number must be bigger than 0")
    @Digits(integer=5,fraction=0,message = "Must be a whole number of maximum 99999")
    private int numberOfTimeUnits;

    @Min(value = 1, message = "Number must be bigger than 0")
    @Digits(integer=5,fraction=0,message = "Must be a whole number of maximum 99999")
    private int numberOfOccurences;

    private boolean enabled;

    public MonitorCommand(String type, TimeUnit timeUnit, int numberOfTimeUnits, boolean enabled, int numberOfOccurences) {
        this.type = type;
        this.timeUnit = timeUnit;
        this.numberOfTimeUnits = numberOfTimeUnits;
        this.enabled = enabled;
        this.numberOfOccurences = numberOfOccurences;
    }

    public MonitorCommand(TimeUnit timeUnit, int numberOfTimeUnits, int numberOfOccurences, boolean enabled) {
        this.timeUnit = timeUnit;
        this.numberOfTimeUnits = numberOfTimeUnits;
        this.numberOfOccurences = numberOfOccurences;
        this.enabled = enabled;
    }

    public MonitorCommand() {
    }

    public MonitorCommand(Monitoring monitor) {
        this.id = monitor.getId();
        this.type = monitor.getClass().getSimpleName();
        this.enabled = monitor.isEnabled();
        if (monitor instanceof FrequencyMonitoring) {
            this.numberOfTimeUnits = ((FrequencyMonitoring)monitor).getNumberOfTimeUnits();
            this.numberOfOccurences = ((FrequencyMonitoring)monitor).getNumberOfOccurences();
        }
        if (monitor instanceof FlatlineMonitoring) {
            this.numberOfTimeUnits = ((FlatlineMonitoring)monitor).getNumberOfTimeUnits();
            this.numberOfOccurences = ((FlatlineMonitoring)monitor).getNumberOfOccurences();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getNumberOfTimeUnits() {
        return numberOfTimeUnits;
    }

    public void setNumberOfTimeUnits(int numberOfTimeUnits) {
        this.numberOfTimeUnits = numberOfTimeUnits;
    }

    public int getNumberOfOccurences() {
        return numberOfOccurences;
    }

    public void setNumberOfOccurences(int numberOfOccurences) {
        this.numberOfOccurences = numberOfOccurences;
    }

    public void toMonitor(Monitoring monitor) {
        monitor.setEnabled(isEnabled());
        if (monitor instanceof FrequencyMonitoring){
            FrequencyMonitoring frequencyMonitor = (FrequencyMonitoring) monitor;
            frequencyMonitor.setTimeUnit(getTimeUnit());
            frequencyMonitor.setNumberOfTimeUnits(getNumberOfTimeUnits());
            frequencyMonitor.setNumberOfOccurences(getNumberOfOccurences());
        }
        if (monitor instanceof FlatlineMonitoring){
            FlatlineMonitoring flatlineMonitor = (FlatlineMonitoring) monitor;
            flatlineMonitor.setTimeUnit(getTimeUnit());
            flatlineMonitor.setNumberOfTimeUnits(getNumberOfTimeUnits());
            flatlineMonitor.setNumberOfOccurences(getNumberOfOccurences());
        }
    }
}
