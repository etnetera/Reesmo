package com.etnetera.projects.testreporting.webapp.elasticsearch;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.util.Assert;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.ElasticAuditedModel;
import com.etnetera.projects.testreporting.webapp.user.UserHelper;

public class CustomSimpleElasticsearchRepository<T> extends SimpleElasticsearchRepository<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomSimpleElasticsearchRepository.class);
	
	public CustomSimpleElasticsearchRepository() {
		super();
	}

	public CustomSimpleElasticsearchRepository(ElasticsearchEntityInformation<T, String> metadata,
			ElasticsearchOperations elasticsearchOperations) {
		super(metadata, elasticsearchOperations);
	}

	public CustomSimpleElasticsearchRepository(ElasticsearchOperations elasticsearchOperations) {
		super(elasticsearchOperations);
	}

	@Override
	public <S extends T> S save(S entity) {
		Assert.notNull(entity, "Cannot save 'null' entity.");
		auditEntity(entity);
		return super.save(entity);
	}

	@Override
	public <S extends T> List<S> save(List<S> entities) {
		Assert.notNull(entities, "Cannot insert 'null' as a List.");
		entities.forEach(e -> auditEntity(e));
		return super.save(entities);
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		Assert.notNull(entities, "Cannot insert 'null' as a List.");
		entities.forEach(e -> auditEntity(e));
		return super.save(entities);
	}

	@SuppressWarnings("unchecked")
	private <S extends T> void auditEntity(S entity) {
		if (!(entity instanceof ElasticAuditedModel)) {
			return;
		}
		
		ElasticAuditedModel e = (ElasticAuditedModel) entity;
		String id = extractIdFromBean((T) e);
		boolean create = id == null;
		Date date = new Date();
		String userId = UserHelper.getUserId();
		
		if (create) {
			e.setCreatedAt(date);
			e.setCreatedBy(userId);
		}
		e.setUpdatedAt(date);
		e.setUpdatedBy(userId);
		
		if (create) {
			LOGGER.debug("Entity " + e.getClass().getSimpleName() + " was created at " + date + " by " + userId);
		} else {
			LOGGER.debug("Entity " + e.getClass().getSimpleName() + " with id " + id + " was updated at " + date + " by " + userId);
		}
	}

}
