package cz.etnetera.reesmo.model.mongodb.monitoring;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "monitoring")
public class AnyMonitoring extends Monitoring {

    @Override
    public String toString() {
        return "Every matching result";
    }

}
