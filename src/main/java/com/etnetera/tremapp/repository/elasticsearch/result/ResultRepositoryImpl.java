package com.etnetera.tremapp.repository.elasticsearch.result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.etnetera.tremapp.list.ListModifier;
import com.etnetera.tremapp.model.ModelAuditor;
import com.etnetera.tremapp.model.elasticsearch.result.Result;
import com.etnetera.tremapp.model.elasticsearch.result.ResultAttachment;
import com.etnetera.tremapp.model.mongodb.view.View;
import com.etnetera.tremapp.repository.mongodb.view.ViewRepository;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

/**
 * Result repository custom method implementation
 */
public class ResultRepositoryImpl implements ResultRepositoryCustom {

	@Autowired
	private ElasticsearchOperations template;

	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
	private ViewRepository viewRepository;
	
	@Autowired
    private GridFsTemplate gridFsTemplate;
	
	@Autowired
	private ModelAuditor modelAuditor;

	@PostConstruct
	private void init() {
        if (!template.indexExists(Result.class)) {
            template.createIndex(Result.class);
        }
	}
	
	@Override
	public Page<Result> findByModifier(ListModifier modifier, List<String> projectIds) {
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
	public Page<Result> findByViewAndModifier(String viewId, ListModifier modifier) {
		View view = viewRepository.findOne(viewId);
		modifier = view.getModifier().join(modifier);
		return findByModifier(modifier, Arrays.asList(view.getProjectId()));
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
		return resultRepository.save(result);
	}

	@Override
	public ResultAttachment createAttachment(Result result, MultipartFile file, String path) throws IOException {
		GridFSFile gridFile = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
		
		ResultAttachment attachment = new ResultAttachment();
		attachment.setId(gridFile.getId().toString());
		attachment.setName(gridFile.getFilename());
		attachment.setPath(StringUtils.trimToNull(path));
		attachment.setContentType(gridFile.getContentType());
		attachment.setSize(gridFile.getLength());
		modelAuditor.audit(attachment);
		
		result.addAttachment(attachment);
		resultRepository.save(result);
		
		return attachment;
	}
	
	@Override
	public ResultAttachment updateAttachment(Result result, String attachmentId, MultipartFile file) throws IOException {
		ResultAttachment attachment = result.getAttachment(attachmentId);
		gridFsTemplate.delete(Query.query(Criteria.where("_id").is(attachment.getId())));
		
		GridFSFile gridFile = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
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

}
