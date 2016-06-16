package cz.etnetera.reesmo.notifier;

import java.util.List;

public class EmailNotifier extends Notifier2 {

    private List<String> addresses;

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    @Override
    public void notifyEveryone() {

    }
}
