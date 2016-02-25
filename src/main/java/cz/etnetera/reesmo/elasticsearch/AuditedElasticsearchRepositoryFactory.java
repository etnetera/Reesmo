package cz.etnetera.reesmo.elasticsearch;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

public class AuditedElasticsearchRepositoryFactory extends ElasticsearchRepositoryFactory {

	private final ElasticsearchOperations localElasticsearchOperations;
	
	public AuditedElasticsearchRepositoryFactory(ElasticsearchOperations elasticsearchOperations) {
		super(elasticsearchOperations);
		this.localElasticsearchOperations = elasticsearchOperations;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected Object getTargetRepository(RepositoryMetadata metadata) {
		ElasticsearchEntityInformation<?, ?> entityInformation = getEntityInformation(metadata.getDomainType());

		Object repository;

		if (entityInformation.getIdType() == String.class) {
			AbstractElasticsearchRepository r = new AuditedSimpleElasticsearchRepository(getEntityInformation(metadata.getDomainType()),
					localElasticsearchOperations);
			r.setEntityClass(metadata.getDomainType());
			repository = r;
		} else {
			repository = super.getTargetRepository(metadata);
		}

		return repository;
	}

}
