package com.etnetera.projects.testreporting.webapp.repository.elasticsearch.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;
import com.etnetera.projects.testreporting.webapp.repository.elasticsearch.QueryWrapper;

public class ResultRepositoryImpl implements ResultRepositoryCustom {

	@Autowired
    private ElasticsearchOperations template;
	
	@Override
	public Page<Result> findByQueryWrapper(QueryWrapper queryWrapper) {
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
                .withTypes("result")
                .withQuery(queryWrapper.getQueryBuilder())
                .withFilter(queryWrapper.getFilterBuilder())
                .withPageable(queryWrapper.getPageable());
		queryWrapper.getSortBuilders().forEach(sb -> builder.withSort(sb));
        return template.queryForPage(builder.build(), Result.class);
	}

	@Override
	public Page<Result> findBySuiteAndQueryWrapper(String suiteId, QueryWrapper queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Result> findByViewAndQueryWrapper(String viewId, QueryWrapper queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

}
