package cz.etnetera.reesmo.list.filter.impl;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.PrefixFilterBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import cz.etnetera.reesmo.list.filter.ListFilter;

/**
 * Prefix filter representation.
 */
@JsonTypeName(PrefixListFilter.TYPE)
public class PrefixListFilter extends ListFilter {

	public static final String TYPE = "prefix";
	
	protected String prefix;

	@Override
	public String getType() {
		return TYPE;
	}

	@JsonIgnore
	@Override
	public FilterBuilder getFilterBuilder() {
		return new BoolFilterBuilder().must(new PrefixFilterBuilder(field, prefix)).cache(true);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
