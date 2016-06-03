package cz.etnetera.reesmo.datatables.view;

import java.util.ArrayList;
import java.util.List;

public class DatatablesViewsDefinition {

	protected String activeViewId;
	
	protected List<DatatablesView> views;

	public DatatablesViewsDefinition() {
		this(null, new ArrayList<>());
	}

	public DatatablesViewsDefinition(String activeViewId, List<DatatablesView> views) {
		this.activeViewId = activeViewId;
		this.views = views;
	}

	public String getActiveViewId() {
		return activeViewId;
	}

	public void setActiveViewId(String activeViewId) {
		this.activeViewId = activeViewId;
	}

	public List<DatatablesView> getViews() {
		return views;
	}

	public void setViews(List<DatatablesView> views) {
		this.views = views;
	}
	
	public DatatablesViewsDefinition addView(DatatablesView view) {
		views.add(view);
		return this;
	}
	
}
