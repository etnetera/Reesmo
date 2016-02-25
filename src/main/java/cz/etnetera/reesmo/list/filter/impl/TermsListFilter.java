package cz.etnetera.reesmo.list.filter.impl;

import java.util.List;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;

import com.fasterxml.jackson.annotation.JsonTypeName;

import cz.etnetera.reesmo.list.filter.ListFilter;

/**
 * Terms filter representation.
 */
@JsonTypeName(TermsListFilter.TYPE)
public class TermsListFilter extends ListFilter {

	public static final String TYPE = "terms";
	
	protected List<Object> terms;

	public FilterBuilder getFilterBuilder() {
		return new BoolFilterBuilder().must(new TermsFilterBuilder(field, terms)).cache(true);
	}

	public List<Object> getTerms() {
		return terms;
	}

	public void setTerms(List<Object> terms) {
		this.terms = terms;
	}
	
}
