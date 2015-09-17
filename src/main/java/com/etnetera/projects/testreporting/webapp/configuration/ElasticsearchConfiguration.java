package com.etnetera.projects.testreporting.webapp.configuration;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.etnetera.projects.testreporting.webapp.elasticsearch.AuditedElasticsearchRepositoryFactoryBean;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.etnetera.projects.testreporting.webapp.repository.elasticsearch", repositoryFactoryBeanClass = AuditedElasticsearchRepositoryFactoryBean.class)
public class ElasticsearchConfiguration {

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int port;

	@Bean
	public Client client() {
		TransportClient client = new TransportClient();
		TransportAddress address = new InetSocketTransportAddress(host, port);
		client.addTransportAddress(address);
		return client;
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(client());
	}

}
