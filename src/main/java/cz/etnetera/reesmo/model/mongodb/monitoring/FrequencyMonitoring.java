package cz.etnetera.reesmo.model.mongodb.monitoring;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Document(collection = "monitoring")
public class FrequencyMonitoring extends Monitoring {

    private TimeUnit timeUnit;

    private int numberOfTimeUnits;

    private int hits;

    private Date from;

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
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

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "Has more than " + hits + " results in " + numberOfTimeUnits + " " +  timeUnit;
    }

}
