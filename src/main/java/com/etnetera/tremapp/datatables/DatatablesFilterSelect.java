package com.etnetera.tremapp.datatables;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.etnetera.tremapp.message.Localizer;

public class DatatablesFilterSelect extends DatatablesFilter {

	public static final String TYPE = "select";

	protected List<Option> options;

	public DatatablesFilterSelect(String field, String label, List<Option> options) {
		super(field, label);
		this.options = options;
	}

	@SuppressWarnings("rawtypes")
	public DatatablesFilterSelect(String field, String labelKey, Enum[] enumValues, String optionMessagePrefix,
			Localizer localizer, Locale locale) {
		this(field, localizer.getMessageSource().getMessage(labelKey, null, locale),
				Stream.of(enumValues)
						.map(enumValue -> new Option(enumValue.name(),
								localizer.getMessageSource()
										.getMessage(optionMessagePrefix + enumValue.name().toLowerCase(), null, locale)))
				.collect(Collectors.toList()));
	}

	@Override
	public String getType() {
		return TYPE;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public static class Option {

		protected String value;

		protected String label;

		public Option(String value, String label) {
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
