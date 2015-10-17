package com.etnetera.tremapp.repository.elasticsearch.result;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.etnetera.tremapp.model.elasticsearch.result.Result;
import com.etnetera.tremapp.model.elasticsearch.result.ResultAttachment;
import com.etnetera.tremapp.utils.list.ListModifier;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * Result repository custom methods
 */
public interface ResultRepositoryCustom {
	
	public Page<Result> findByModifier(ListModifier modifier, List<String> allowedProjectIds);
	
	public Page<Result> findBySuiteAndModifier(String suiteId, ListModifier modifier, List<String> allowedProjectIds);
	
	public Page<Result> findByViewAndModifier(String viewId, ListModifier modifier, List<String> allowedProjectIds);
	
	/**
	 * Overrides default delete method, so attachments from GridFS
	 * are deleted as well. They should be deleted after
	 * delete of result is completed.
	 * 
	 * @param result
	 */
	public void deleteResult(Result result);
	
	/**
	 * Save result with specific attachments, overriding every existing.
	 * It does not really add/remove file from attachments, it just updates
	 * attachments list.
	 * 
	 * @param result
	 * @param attachments
	 * @return
	 */
	public Result saveResult(Result result, List<ResultAttachment> attachments);
	
	/**
	 * Creates new attachment.
	 * 
	 * @param result
	 * @param file
	 * @param path 
	 * @return
	 * @throws IOException 
	 */
	public ResultAttachment createAttachment(Result result, MultipartFile file, String path) throws IOException;
	
	/**
	 * Updates existing attachment.
	 * 
	 * @param result
	 * @param attachmentId
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public ResultAttachment updateAttachment(Result result, String attachmentId, MultipartFile file) throws IOException;
	
	/**
	 * Deletes existing attachment.
	 * 
	 * @param result
	 * @param attachmentId
	 */
	public void deleteAttachment(Result result, String attachmentId);
	
	/**
	 * Returns file for attachment.
	 * 
	 * @param attachment
	 * @return
	 */
	public GridFSDBFile getAttachmentFile(ResultAttachment attachment);
	
}
