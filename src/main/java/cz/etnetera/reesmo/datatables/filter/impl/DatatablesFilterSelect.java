package cz.etnetera.reesmo.datatables.filter.impl;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cz.etnetera.reesmo.datatables.filter.DatatablesFilter;
import cz.etnetera.reesmo.message.Localizer;

public class DatatablesFilterSelect extends DatatablesFilter {

	public static final String TYPE = "select";

	protected List<Option> options;

	public DatatablesFilterSelect(String field, String label, List<Option> options) {
		super(field, label);
		this.options = options;
	}

	public DatatablesFilterSelect(String field, String labelKey, List<String> values, Localizer localizer,
			Locale locale) {
		this(field, localizer.getMessageSource().getMessage(labelKey, null, locale),
				values.stream().map(value -> new Option(value, value)).collect(Collectors.toList()));
	}

	public DatatablesFilterSelect(String field, String labelKey, List<String> values, String optionMessagePrefix,
			Localizer localizer, Locale locale) {
		this(field, localizer.getMessageSource().getMessage(labelKey, null, locale),
				values.stream()
						.map(value -> new Option(value, localizer.getMessageSource()
								.getMessage(optionMessagePrefix + value.toLowerCase(), null, locale)))
				.collect(Collectors.toList()));
	}

	@SuppressWarnings("rawtypes")
	public DatatablesFilterSelect(String field, String labelKey, Enum[] enumValues, Localizer localizer,
			Locale locale) {
		this(field, labelKey, Stream.of(enumValues).map(enumValue -> enumValue.name()).collect(Collectors.toList()),
				localizer, locale);
	}

	@SuppressWarnings("rawtypes")
	public DatatablesFilterSelect(String field, String labelKey, Enum[] enumValues, String optionMessagePrefix,
			Localizer localizer, Locale locale) {
		this(field, labelKey, Stream.of(enumValues).map(enumValue -> enumValue.name()).collect(Collectors.toList()),
				optionMessagePrefix, localizer, locale);
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
