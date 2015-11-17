package com.etnetera.tremapp.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.tremapp.list.ListModifier;
import com.etnetera.tremapp.model.elasticsearch.result.Result;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.elasticsearch.result.ResultRepository;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.restapi.output.RestApiList;
import com.etnetera.tremapp.user.UserManager;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class ResultRestApiController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@RequestMapping(value = "/results/create", method = RequestMethod.POST)
	public Result createResult(@RequestBody Result result) {
		userManager.checkProjectPermission(result.getProjectId(), Permission.EDITOR);
		// save result without attachments
		return resultRepository.saveResult(result, null);
	}
	
	@RequestMapping(value = "/results/create/{projectKey}", method = RequestMethod.POST)
	public Result createResult(@PathVariable String projectKey, @RequestBody Result result) {
		Project project = projectRepository.findOneByKey(projectKey);
		userManager.checkProjectPermission(project.getId(), Permission.EDITOR);
		result.setProjectId(project.getId());
		// save result without attachments
		return resultRepository.saveResult(result, null);
	}
	
	@RequestMapping(value = "/results/get/{resultId}", method = RequestMethod.GET)
	public Result getResult(@PathVariable String resultId) {
		Result result = resultRepository.findOne(resultId); 
		userManager.checkProjectPermission(result.getProjectId(), Permission.BASIC);
		return result;
	}
	
	@RequestMapping(value = "/results/update/{resultId}", method = RequestMethod.POST)
	public Result updateResult(@PathVariable String resultId, @RequestBody Result result) {
		Result persistedResult = resultRepository.findOne(resultId);
		userManager.checkProjectPermission(persistedResult.getProjectId(), Permission.EDITOR);
		if (result.getProjectId() == null)
			result.setProjectId(persistedResult.getProjectId());
		else if (!result.getProjectId().equals(persistedResult.getProjectId()))
			userManager.checkProjectPermission(result.getProjectId(), Permission.EDITOR);
		result.setId(resultId);
		// save result without modifying attachments
		return resultRepository.saveResult(result, persistedResult.getAttachments());
	}
	
	@RequestMapping(value = "/results/delete/{resultId}", method = RequestMethod.GET)
	public void deleteResult(@PathVariable String resultId) {
		Result result = resultRepository.findOne(resultId);
		userManager.checkProjectPermission(result.getProjectId(), Permission.EDITOR);
		resultRepository.deleteResult(result);
	}
	
	@RequestMapping(value = "/results/list", method = RequestMethod.GET)
	public RestApiList<Result> getResults() {
		return new RestApiList<>(resultRepository.findByModifier(new ListModifier(), userManager.getAllowedProjectIds(Permission.BASIC)));
	}
	
	@RequestMapping(value = "/results/list", method = RequestMethod.POST)
	public RestApiList<Result> searchResults(@RequestBody ListModifier modifier) {
		return new RestApiList<>(resultRepository.findByModifier(modifier, userManager.getAllowedProjectIds(Permission.BASIC)));
	}
	
}
