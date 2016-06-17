package cz.etnetera.reesmo.datatables.view;

import cz.etnetera.reesmo.model.mongodb.view.View;

public class DatatablesView {

	protected String id;
	
	protected String name;
	
	protected String description;

	public static DatatablesView fromView(View view) {
		return new DatatablesView(view.getId(), view.getName(), view.getDescription());
	}
	
	public DatatablesView(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
