package cz.etnetera.reesmo.model.mongodb.notifier;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "notifier")
public class EmailNotifier extends Notifier {

    private List<String> addresses;

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

}
