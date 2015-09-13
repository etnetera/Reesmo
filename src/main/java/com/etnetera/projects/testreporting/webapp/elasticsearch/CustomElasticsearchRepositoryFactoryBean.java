package com.etnetera.projects.testreporting.webapp.elasticsearch;

import java.io.Serializable;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

public class CustomElasticsearchRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
		extends RepositoryFactoryBeanSupport<T, S, ID> {

	private ElasticsearchOperations operations;

	/**
	 * Configures the {@link ElasticsearchOperations} to be used to create Elasticsearch repositories.
	 *
	 * @param operations the operations to set
	 */
	public void setElasticsearchOperations(ElasticsearchOperations operations) {
		Assert.notNull(operations);
		this.operations = operations;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(operations, "ElasticsearchOperations must be configured!");
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory() {
		return new CustomElasticsearchRepositoryFactory(operations);
	}

}
