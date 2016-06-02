package cz.etnetera.reesmo.model.datatables.view;

public class ViewDT {

    private String id;

    private String name;

    private String key;

    private String description;

    public ViewDT(String id, String name, String key, String description) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.description = description;
    }

    public String getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
