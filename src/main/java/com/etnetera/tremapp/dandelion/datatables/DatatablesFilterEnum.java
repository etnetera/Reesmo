package com.etnetera.tremapp.dandelion.datatables;

import java.util.List;

public class DatatablesFilterEnum extends DatatablesFilter {

	public static final String TYPE = "enum";
	
	protected List<Value> values;

	public DatatablesFilterEnum(String field, String label, List<Value> values) {
		super(field, label);
		this.values = values;
	}

	@Override
	public String getType() {
		return TYPE;
	}
	
	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

	public static class Value {
		
		protected String value;
		
		protected String label;

		public Value(String value, String label) {
			this.value = value;
			this.label = label;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
		
	}
	
}
