package cz.etnetera.reesmo.list.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.etnetera.reesmo.list.filter.impl.*;
import org.elasticsearch.index.query.FilterBuilder;

/**
 * Filter representation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
	@JsonSubTypes.Type(value = PrefixListFilter.class, name = PrefixListFilter.TYPE),
	@JsonSubTypes.Type(value = TermListFilter.class, name = TermListFilter.TYPE),
	@JsonSubTypes.Type(value = TermsListFilter.class, name = TermsListFilter.TYPE),
	@JsonSubTypes.Type(value = DoubleRangeListFilter.class, name = DoubleRangeListFilter.TYPE),
	@JsonSubTypes.Type(value = DateRangeListFilter.class, name = DateRangeListFilter.TYPE)
}) 
abstract public class ListFilter {

	protected String field;
	
	abstract public String getType();
	
	@JsonIgnore
	abstract public FilterBuilder getFilterBuilder();

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
}
