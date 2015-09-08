package com.etnetera.projects.testreporting.webapp.repository.elasticsearch.result;

import java.util.List;

import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;
import com.etnetera.projects.testreporting.webapp.model.mongodb.view.View;
import com.etnetera.projects.testreporting.webapp.repository.mongodb.view.ViewRepository;
import com.etnetera.projects.testreporting.webapp.utils.list.ListModifier;

/**
 * Result repository custom method implementation
 */
public class ResultRepositoryImpl implements ResultRepositoryCustom {

	@Autowired
	private ElasticsearchOperations template;

	@Autowired
	private ViewRepository viewRepository;

	@Override
	public Page<Result> findByModifier(ListModifier modifier, List<String> allowedProjectIds) {
		if (allowedProjectIds == null) {
			return template.queryForPage(createSearchBuilderFromModifier(modifier).build(), Result.class);
		}
		return template
				.queryForPage(createSearchBuilderFromModifier(modifier,
						new AndFilterBuilder()
								.add(new BoolFilterBuilder()
										.must(new TermsFilterBuilder("projectId", allowedProjectIds)).cache(true))
								.add(modifier.getFilterBuilder())).build(),
						Result.class);
	}

	@Override
	public Page<Result> findBySuiteAndModifier(String suiteId, ListModifier modifier, List<String> allowedProjectIds) {
		AndFilterBuilder aFB = new AndFilterBuilder()
				.add(new BoolFilterBuilder().must(new TermFilterBuilder("suiteId", suiteId)).cache(true))
				.add(modifier.getFilterBuilder());

		if (allowedProjectIds != null) {
			aFB.add(new BoolFilterBuilder().must(new TermsFilterBuilder("projectId", allowedProjectIds)).cache(true));
		}

		return template.queryForPage(createSearchBuilderFromModifier(modifier, aFB).build(), Result.class);
	}

	@Override
	public Page<Result> findByViewAndModifier(String viewId, ListModifier modifier, List<String> allowedProjectIds) {
		View view = viewRepository.findOne(viewId);
		modifier = view.getModifier().join(modifier);
		return findByModifier(modifier, allowedProjectIds);
	}

	private NativeSearchQueryBuilder createSearchBuilderFromModifier(ListModifier modifier) {
		return createSearchBuilderFromModifier(modifier, null);
	}

	private NativeSearchQueryBuilder createSearchBuilderFromModifier(ListModifier modifier,
			FilterBuilder filterBuilder) {
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withTypes("result")
				.withFilter(filterBuilder == null ? modifier.getFilterBuilder() : filterBuilder)
				.withPageable(modifier.getPageable());
		modifier.getSortBuilders().forEach(sb -> builder.withSort(sb));
		return builder;
	}

}
