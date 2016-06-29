package cz.etnetera.reesmo.model.mongodb.monitoring;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.TimeUnit;

@Document(collection = "monitoring")
public class FlatlineMonitoring extends Monitoring {

    private TimeUnit timeUnit;

    private int numberOfTimeUnits;

    private int numberOfOccurences;

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getNumberOfOccurences() {
        return numberOfOccurences;
    }

    public void setNumberOfOccurences(int numberOfOccurences) {
        this.numberOfOccurences = numberOfOccurences;
    }

    public int getNumberOfTimeUnits() {
        return numberOfTimeUnits;
    }

    public void setNumberOfTimeUnits(int numberOfTimeUnits) {
        this.numberOfTimeUnits = numberOfTimeUnits;
    }

    @Override
    public String toString() {
        return "Hass less than " + numberOfOccurences + " results in " + numberOfOccurences + " " + timeUnit;
    }

}
