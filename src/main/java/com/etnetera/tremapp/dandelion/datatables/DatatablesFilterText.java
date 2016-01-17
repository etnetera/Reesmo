package com.etnetera.tremapp.dandelion.datatables;

public class DatatablesFilterText extends DatatablesFilter {

	public static final String TYPE = "text";
	
	public DatatablesFilterText(String field, String label) {
		super(field, label);
	}

	@Override
	public String getType() {
		return TYPE;
	}
	
}
