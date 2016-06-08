package cz.etnetera.reesmo.list.filter.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import cz.etnetera.reesmo.list.filter.ListFilter;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;

/**
 * Term filter representation.
 */
@JsonTypeName(TermListFilter.TYPE)
public class TermListFilter extends ListFilter {

	public static final String TYPE = "term";
	
	protected Object term;

	@Override
	public String getType() {
		return TYPE;
	}

	@JsonIgnore
	@Override
	public FilterBuilder getFilterBuilder() {
		return new BoolFilterBuilder().must(new TermFilterBuilder(field, term)).cache(true);
	}

	public Object getTerm() {
		return term;
	}

	public void setTerm(Object term) {
		this.term = term;
	}

}
