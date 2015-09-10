package com.etnetera.projects.testreporting.webapp.repository.elasticsearch.suite;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.suite.Suite;
import com.etnetera.projects.testreporting.webapp.utils.list.ListModifier;

/**
 * Suite repository custom method implementation
 */
public class SuiteRepositoryImpl implements SuiteRepositoryCustom {

	@Autowired
	private ElasticsearchOperations template;

	@Override
	public Page<Suite> findByModifier(ListModifier modifier, List<String> allowedProjectIds) {
		if (allowedProjectIds == null) {
			return template.queryForPage(createSearchBuilderFromModifier(modifier).build(), Suite.class);
		}
		if (allowedProjectIds.isEmpty()) {
			return new PageImpl<>(new ArrayList<>());
		}

		return template.queryForPage(
				createSearchBuilderFromModifier(modifier,
						modifier.getFilterBuilder(new BoolFilterBuilder()
								.must(new TermsFilterBuilder("projectId", allowedProjectIds)).cache(true))).build(),
				Suite.class);
	}

	private NativeSearchQueryBuilder createSearchBuilderFromModifier(ListModifier modifier) {
		return createSearchBuilderFromModifier(modifier, null);
	}

	private NativeSearchQueryBuilder createSearchBuilderFromModifier(ListModifier modifier,
			FilterBuilder filterBuilder) {
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withTypes("suite")
				.withFilter(filterBuilder == null ? modifier.getFilterBuilder() : filterBuilder)
				.withPageable(modifier.getPageable());
		modifier.getSortBuilders().forEach(sb -> builder.withSort(sb));
		return builder;
	}

}
