package cz.etnetera.reesmo.elasticsearch;

import cz.etnetera.reesmo.model.ModelAuditor;
import cz.etnetera.reesmo.model.elasticsearch.ElasticAuditedModel;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.util.Assert;

import java.util.List;

public class AuditedSimpleElasticsearchRepository<T> extends SimpleElasticsearchRepository<T> {
	
	public AuditedSimpleElasticsearchRepository() {
		super();
	}

	public AuditedSimpleElasticsearchRepository(ElasticsearchEntityInformation<T, String> metadata,
			ElasticsearchOperations elasticsearchOperations) {
		super(metadata, elasticsearchOperations);
	}

	public AuditedSimpleElasticsearchRepository(ElasticsearchOperations elasticsearchOperations) {
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

	protected <S extends T> void auditEntity(S entity) {
		if (!(entity instanceof ElasticAuditedModel)) {
			return;
		}
		
		ModelAuditor.getInstance().audit((ElasticAuditedModel) entity);
	}

}
