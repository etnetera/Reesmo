package com.etnetera.tremapp.web.utils.list;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;

/**
 * Filter representation.
 * 
 * @author zdenek
 *
 */
public class ListFilter {

	private String field;
	
	private String comparator;
	
	private Object value;
	
	@SuppressWarnings("unchecked")
	public FilterBuilder getFilterBuilder() {
		if (StringUtils.isEmpty(comparator) || "TERM".equalsIgnoreCase(comparator)) {
			return new BoolFilterBuilder().must(new TermFilterBuilder(field, value)).cache(true);
		}
		if ("RANGE".equalsIgnoreCase(comparator)) {
			List<Object> ranges = (List<Object>) value;
			return new BoolFilterBuilder().must(new RangeFilterBuilder(field).from(ranges.get(0)).to(ranges.get(1))).cache(true);
		}
		throw new IllegalArgumentException("Unknown list filter comparator: " + comparator);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getComparator() {
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
