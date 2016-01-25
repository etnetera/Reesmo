package com.etnetera.tremapp.datatables.filter.impl;

import java.util.Locale;

import com.etnetera.tremapp.datatables.filter.DatatablesFilter;
import com.etnetera.tremapp.message.Localizer;

public class DatatablesFilterText extends DatatablesFilter {

	public static final String TYPE = "text";
	
	public DatatablesFilterText(String field, String label) {
		super(field, label);
	}
	
	public DatatablesFilterText(String field, String labelKey, Localizer localizer, Locale locale) {
		this(field, localizer.getMessageSource().getMessage(labelKey, null, locale));
	}

	@Override
	public String getType() {
		return TYPE;
	}
	
}
