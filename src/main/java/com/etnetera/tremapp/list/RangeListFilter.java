package com.etnetera.tremapp.list;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Range filter representation.
 */
@JsonTypeName(RangeListFilter.TYPE)
public class RangeListFilter extends ListFilter {

	public static final String TYPE = "range";
	
	protected Object from;
	
	protected Object to;

	public FilterBuilder getFilterBuilder() {
		return new BoolFilterBuilder().must(new RangeFilterBuilder(field).from(from).to(to)).cache(true);
	}

	public Object getFrom() {
		return from;
	}

	public void setFrom(Object from) {
		this.from = from;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}

}
