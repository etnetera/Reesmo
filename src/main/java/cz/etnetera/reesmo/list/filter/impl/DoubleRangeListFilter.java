package cz.etnetera.reesmo.list.filter.impl;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;

import com.fasterxml.jackson.annotation.JsonTypeName;

import cz.etnetera.reesmo.list.filter.ListFilter;

/**
 * Double range filter representation.
 */
@JsonTypeName(DoubleRangeListFilter.TYPE)
public class DoubleRangeListFilter extends ListFilter {

	public static final String TYPE = "doublerange";
	
	protected Double from;
	
	protected Double to;

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public FilterBuilder getFilterBuilder() {
		RangeFilterBuilder builder = new RangeFilterBuilder(field);
		if (from != null)
			builder.from(from.doubleValue());
		if (to != null)
			builder.to(to.doubleValue());
		return new BoolFilterBuilder().must(builder).cache(true);
	}

	public Double getFrom() {
		return from;
	}

	public void setFrom(Double from) {
		this.from = from;
	}

	public Double getTo() {
		return to;
	}

	public void setTo(Double to) {
		this.to = to;
	}

}
