package cz.etnetera.reesmo.restapi;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cz.etnetera.reesmo.list.PageableListModifier;
import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.elasticsearch.result.ResultValidator;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.repository.elasticsearch.result.ResultRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.restapi.output.RestApiList;
import cz.etnetera.reesmo.user.UserManager;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class ResultRestApiController extends AbstractRestController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@InitBinder(value = "result")
	protected void initBinder(WebDataBinder binder) {		
		binder.addValidators(new ResultValidator());
	}
	
	@RequestMapping(value = "/results/create", method = RequestMethod.POST)
	public Result createResult(@RequestBody @Valid Result result) {
		userManager.checkProjectPermission(result.getProjectId(), Permission.EDITOR);
		// save result without attachments
		return resultRepository.saveResult(result, null);
	}
	
	@RequestMapping(value = "/results/create/{projectKey}", method = RequestMethod.POST)
	public Result createResult(@RequestBody @Valid Result result, @PathVariable String projectKey) {
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
	public Result updateResult(@RequestBody @Valid Result result, @PathVariable String resultId) {
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
		return new RestApiList<>(resultRepository.findByModifier(new PageableListModifier(), userManager.getAllowedProjectIds(Permission.BASIC)));
	}
	
	@RequestMapping(value = "/results/list", method = RequestMethod.POST)
	public RestApiList<Result> searchResults(@RequestBody PageableListModifier modifier) {
		return new RestApiList<>(resultRepository.findByModifier(modifier, userManager.getAllowedProjectIds(Permission.BASIC)));
	}
	
}
