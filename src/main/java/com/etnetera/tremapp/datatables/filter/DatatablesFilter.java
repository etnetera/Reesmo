package com.etnetera.tremapp.datatables.filter;

abstract public class DatatablesFilter {

	protected String field;
	
	protected String label;

	public DatatablesFilter(String field, String label) {
		this.field = field;
		this.label = label;
	}

	abstract public String getType();
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
