package com.etnetera.tremapp.list;

import org.elasticsearch.index.query.FilterBuilder;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Filter representation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
	@JsonSubTypes.Type(value = PrefixListFilter.class, name = PrefixListFilter.TYPE),
	@JsonSubTypes.Type(value = TermListFilter.class, name = TermListFilter.TYPE),
	@JsonSubTypes.Type(value = TermsListFilter.class, name = TermsListFilter.TYPE),
	@JsonSubTypes.Type(value = RangeListFilter.class, name = RangeListFilter.TYPE)
}) 
abstract public class ListFilter {

	protected String field;
	
	abstract public FilterBuilder getFilterBuilder();

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
}
