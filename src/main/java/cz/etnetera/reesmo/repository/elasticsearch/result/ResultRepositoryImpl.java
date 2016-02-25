package cz.etnetera.reesmo.repository.elasticsearch.result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import cz.etnetera.reesmo.datatables.filter.FilteredDatatablesCriterias;
import cz.etnetera.reesmo.list.PageableListModifier;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.ModelAuditor;
import cz.etnetera.reesmo.model.datatables.result.ResultDT;
import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.elasticsearch.result.ResultAttachment;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.elasticsearch.ElasticsearchDatatables;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;

/**
 * Result repository custom method implementation
 */
public class ResultRepositoryImpl implements ResultRepositoryCustom {

	@Autowired
	private ElasticsearchOperations template;

	@Autowired
	private ResultRepository resultRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ViewRepository viewRepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private ModelAuditor modelAuditor;

	@Autowired
	private Localizer localizer;

	@PostConstruct
	private void init() {
		if (!template.indexExists(Result.class)) {
			template.createIndex(Result.class);
		}
	}

	@Override
	public Page<Result> findByModifier(PageableListModifier modifier, List<String> projectIds) {
		if (projectIds == null) {
			return template.queryForPage(createSearchBuilderFromModifier(modifier).build(), Result.class);
		}
		if (projectIds.isEmpty()) {
			return new PageImpl<>(new ArrayList<>());
		}

		return template.queryForPage(
				createSearchBuilderFromModifier(modifier,
						modifier.getFilterBuilder(new BoolFilterBuilder()
								.must(new TermsFilterBuilder("projectId", projectIds)).cache(true))).build(),
				Result.class);
	}

	@Override
	public Page<Result> findByViewAndModifier(String viewId, PageableListModifier modifier) {
		View view = viewRepository.findOne(viewId);
		modifier = new PageableListModifier().join(view.getModifier()).join(modifier);
		return findByModifier(modifier, Arrays.asList(view.getProjectId()));
	}

	private NativeSearchQueryBuilder createSearchBuilderFromModifier(PageableListModifier modifier) {
		return createSearchBuilderFromModifier(modifier, null);
	}

	private NativeSearchQueryBuilder createSearchBuilderFromModifier(PageableListModifier modifier,
			FilterBuilder filterBuilder) {
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withTypes("result")
				.withFilter(filterBuilder == null ? modifier.getFilterBuilder() : filterBuilder)
				.withPageable(modifier.getPageable());
		modifier.getSortBuilders().forEach(sb -> builder.withSort(sb));
		return builder;
	}

	@Override
	public void deleteResult(Result result) {
		Assert.notNull(result, "Cannot delete 'null' result.");
		List<ResultAttachment> attachments = result.getAttachments();
		resultRepository.delete(result);
		attachments.forEach(a -> gridFsTemplate.delete(Query.query(Criteria.where("_id").is(a.getId()))));
	}

	@Override
	public Result saveResult(Result result, List<ResultAttachment> attachments) {
		result.setAttachments(attachments == null ? new ArrayList<>() : attachments);
		if (result.getStartedAt() != null) {
			if (result.getEndedAt() != null) result.setLength(result.getEndedAt().getTime() - result.getStartedAt().getTime());
			else if (result.getLength() != null) result.setEndedAt(new Date(result.getStartedAt().getTime() + result.getLength()));
		}
		return resultRepository.save(result);
	}

	@Override
	public ResultAttachment createAttachment(Result result, MultipartFile file, String path) throws IOException {
		GridFSFile gridFile = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(),
				file.getContentType());

		ResultAttachment attachment = new ResultAttachment();
		attachment.setId(gridFile.getId().toString());
		attachment.setName(gridFile.getFilename());
		attachment.setPath(path == null ? gridFile.getFilename() : StringUtils.trim(path));
		attachment.setContentType(gridFile.getContentType());
		attachment.setSize(gridFile.getLength());
		modelAuditor.audit(attachment);

		result.addAttachment(attachment);
		resultRepository.save(result);

		return attachment;
	}

	@Override
	public ResultAttachment updateAttachment(Result result, String attachmentId, MultipartFile file)
			throws IOException {
		ResultAttachment attachment = result.getAttachment(attachmentId);
		gridFsTemplate.delete(Query.query(Criteria.where("_id").is(attachment.getId())));

		GridFSFile gridFile = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(),
				file.getContentType());
		attachment.setId(gridFile.getId().toString());
		attachment.setName(gridFile.getFilename());
		attachment.setContentType(gridFile.getContentType());
		attachment.setSize(gridFile.getLength());
		modelAuditor.audit(attachment);

		resultRepository.save(result);

		return attachment;
	}

	@Override
	public void deleteAttachment(Result result, String attachmentId) {
		ResultAttachment attachment = result.getAttachment(attachmentId);
		gridFsTemplate.delete(Query.query(Criteria.where("_id").is(attachment.getId())));
		result.removeAttachment(attachment);
		resultRepository.save(result);
	}

	@Override
	public GridFSDBFile getAttachmentFile(ResultAttachment attachment) {
		return gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(attachment.getId())));
	}

	@Override
	public DataSet<ResultDT> findWithDatatablesCriterias(DatatablesCriterias criterias, List<String> projectIds,
			Locale locale) {
		DataSet<Result> results = findResultsWithDatatablesCriterias(criterias, projectIds);

		Set<String> foundProjectIds = results.getRows().stream().filter(r -> r.getProjectId() != null)
				.map(r -> r.getProjectId()).collect(Collectors.toSet());
		Map<String, Project> foundProjects = StreamSupport
				.stream(projectRepository.findAll(foundProjectIds).spliterator(), false)
				.collect(Collectors.toMap(Project::getId, Function.identity()));

		return new DataSet<ResultDT>(results.getRows().stream()
				.map(r -> new ResultDT(r, foundProjects.get(r.getProjectId()), localizer, locale))
				.collect(Collectors.toList()), results.getTotalRecords(), results.getTotalDisplayRecords());
	}
	
	@Override
	public DataSet<ResultDT> findWithFilteredDatatablesCriterias(FilteredDatatablesCriterias criterias, List<String> projectIds,
			Locale locale) {
		DataSet<Result> results = findResultsWithFilteredDatatablesCriterias(criterias, projectIds);

		Set<String> foundProjectIds = results.getRows().stream().filter(r -> r.getProjectId() != null)
				.map(r -> r.getProjectId()).collect(Collectors.toSet());
		Map<String, Project> foundProjects = StreamSupport
				.stream(projectRepository.findAll(foundProjectIds).spliterator(), false)
				.collect(Collectors.toMap(Project::getId, Function.identity()));

		return new DataSet<ResultDT>(results.getRows().stream()
				.map(r -> new ResultDT(r, foundProjects.get(r.getProjectId()), localizer, locale))
				.collect(Collectors.toList()), results.getTotalRecords(), results.getTotalDisplayRecords());
	}

	private DataSet<Result> findResultsWithDatatablesCriterias(DatatablesCriterias criterias,
			Collection<String> projectIds) {
		FilterBuilder filterBuilder = null;
		if (projectIds != null) {
			if (projectIds.isEmpty()) {
				return new DataSet<Result>(new ArrayList<>(), 0L, 0L);
			} else {
				filterBuilder = new BoolFilterBuilder().must(new TermsFilterBuilder("projectId", projectIds))
						.cache(true);
			}
		}

		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withTypes("result");
		if (filterBuilder != null) {
			queryBuilder.withFilter(filterBuilder);
		}
		queryBuilder.withPageable(ElasticsearchDatatables.getPageable(criterias));
		ElasticsearchDatatables.sortQueryBuilder(queryBuilder, criterias);

		Page<Result> projects = template.queryForPage(queryBuilder.build(), Result.class);
		return new DataSet<Result>(projects.getContent(), Long.valueOf(projects.getNumberOfElements()),
				projects.getTotalElements());
	}
	
	private DataSet<Result> findResultsWithFilteredDatatablesCriterias(FilteredDatatablesCriterias criterias,
			Collection<String> projectIds) {
		FilterBuilder filterBuilder = null;
		if (projectIds != null) {
			if (projectIds.isEmpty()) {
				return new DataSet<Result>(new ArrayList<>(), 0L, 0L);
			} else {
				filterBuilder = new BoolFilterBuilder().must(new TermsFilterBuilder("projectId", projectIds))
						.cache(true);
			}
		}

		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withTypes("result");
		if (filterBuilder != null) {
			queryBuilder.withFilter(criterias.getFilterBuilder(filterBuilder));
		} else {
			queryBuilder.withFilter(criterias.getFilterBuilder());
		}
		queryBuilder.withPageable(ElasticsearchDatatables.getPageable(criterias.getCriterias()));
		ElasticsearchDatatables.sortQueryBuilder(queryBuilder, criterias.getCriterias());

		Page<Result> projects = template.queryForPage(queryBuilder.build(), Result.class);
		return new DataSet<Result>(projects.getContent(), Long.valueOf(projects.getNumberOfElements()),
				projects.getTotalElements());
	}

}
