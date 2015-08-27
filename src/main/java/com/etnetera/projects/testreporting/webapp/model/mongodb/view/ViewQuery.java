package com.etnetera.projects.testreporting.webapp.model.mongodb.view;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * Elastic search query representation.
 * 
 * @author zdenek
 *
 */
public class ViewQuery {

	private String query;
	
	public QueryBuilder getQueryBuilder() {
		// TODO implement
		return null;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}
