package cz.etnetera.reesmo.model.datatables.view;

import cz.etnetera.reesmo.model.mongodb.view.View;

public class ViewDT {

    private String id;

    private String name;

    private String description;

    public ViewDT(View view) {
        this.id = view.getId();
        this.name = view.getName();
        this.description = view.getDescription();
    }

    public String getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
