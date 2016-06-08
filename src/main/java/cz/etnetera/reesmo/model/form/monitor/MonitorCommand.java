package cz.etnetera.reesmo.model.form.monitor;

import cz.etnetera.reesmo.model.mongodb.monitoring.FlatlineMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.util.concurrent.TimeUnit;

public class MonitorCommand {


    private TimeUnit timeUnit;

    @Min(value = 1, message = "Number must be bigger than 0")
    @Digits(integer=5,fraction=0,message = "Must be a whole number of maximum length 5")
    private int numberOfTimeUnits;

    @Min(value = 1, message = "Number must be bigger than 0")
    @Digits(integer=5,fraction=0,message = "Must be a whole number of maximum length 5")
    private int numberOfOccurences;

    public MonitorCommand(TimeUnit timeUnit, Integer numberOfTimeUnits, Integer numberOfOccurences) {
        this.timeUnit = timeUnit;
        this.numberOfTimeUnits = numberOfTimeUnits;
        this.numberOfOccurences = numberOfOccurences;
    }

    public MonitorCommand() {
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
