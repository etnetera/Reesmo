package com.etnetera.projects.testreporting.webapp.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;
import com.etnetera.projects.testreporting.webapp.model.elasticsearch.suite.Suite;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.Permission;
import com.etnetera.projects.testreporting.webapp.model.mongodb.view.View;
import com.etnetera.projects.testreporting.webapp.repository.elasticsearch.result.ResultRepository;
import com.etnetera.projects.testreporting.webapp.repository.elasticsearch.suite.SuiteRepository;
import com.etnetera.projects.testreporting.webapp.repository.mongodb.view.ViewRepository;
import com.etnetera.projects.testreporting.webapp.restapi.output.RestApiList;
import com.etnetera.projects.testreporting.webapp.user.UserHelper;
import com.etnetera.projects.testreporting.webapp.utils.list.ListModifier;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class ResultRestApiController {
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
	private SuiteRepository suiteRepository;
	
	@Autowired
	private ViewRepository viewRepository;
	
	@RequestMapping(value = "/results/create", method = RequestMethod.POST)
	public Result createResult(@RequestBody Result result) {
		UserHelper.checkProjectPermission(result.getProjectId(), Permission.EDITOR);
		// save result without attachments
		return resultRepository.save(result, null);
	}
	
	@RequestMapping(value = "/results/get/{resultId}", method = RequestMethod.GET)
	public Result getResult(@PathVariable String resultId) {
		Result result = resultRepository.findOne(resultId); 
		UserHelper.checkProjectPermission(result.getProjectId(), Permission.BASIC);
		return result;
	}
	
	@RequestMapping(value = "/results/update/{resultId}", method = RequestMethod.POST)
	public Result updateResult(@PathVariable String resultId, @RequestBody Result result) {
		Result persistedResult = resultRepository.findOne(resultId);
		UserHelper.checkProjectPermission(persistedResult.getProjectId(), Permission.EDITOR);
		UserHelper.checkProjectPermission(result.getProjectId(), Permission.EDITOR);
		result.setId(resultId);
		// save result without modifying attachments
		return resultRepository.save(result, persistedResult.getAttachments());
	}
	
	@RequestMapping(value = "/results/delete/{resultId}", method = RequestMethod.GET)
	public void deleteResult(@PathVariable String resultId) {
		Result result = resultRepository.findOne(resultId);
		UserHelper.checkProjectPermission(result.getProjectId(), Permission.EDITOR);
		resultRepository.delete(result);
	}
	
	@RequestMapping(value = "/results/list", method = RequestMethod.GET)
	public RestApiList<Result> getResults() {
		return new RestApiList<>(resultRepository.findByModifier(new ListModifier(), UserHelper.getAllowedProjectIds(Permission.BASIC)));
	}
	
	@RequestMapping(value = "/suite-results/list/{suiteId}", method = RequestMethod.GET)
	public RestApiList<Result> getSuiteResults(@PathVariable String suiteId) {
		Suite suite = suiteRepository.findOne(suiteId);
		UserHelper.checkProjectPermission(suite.getProjectId(), Permission.BASIC);
		return new RestApiList<>(resultRepository.findBySuiteAndModifier(suiteId, new ListModifier(), UserHelper.getAllowedProjectIds(Permission.BASIC)));
	}
	
	@RequestMapping(value = "/view-results/list/{viewId}", method = RequestMethod.GET)
	public RestApiList<Result> getViewResults(@PathVariable String viewId) {
		View view = viewRepository.findOne(viewId);
		view.checkUserPermission(UserHelper.getUser(), Permission.BASIC);
		return new RestApiList<>(resultRepository.findByViewAndModifier(viewId, new ListModifier(), UserHelper.getAllowedProjectIds(Permission.BASIC)));
	}
	
	@RequestMapping(value = "/results/list", method = RequestMethod.POST)
	public RestApiList<Result> searchResults(@RequestBody ListModifier modifier) {
		return new RestApiList<>(resultRepository.findByModifier(modifier, UserHelper.getAllowedProjectIds(Permission.BASIC)));
	}
	
	@RequestMapping(value = "/suite-results/list/{suiteId}", method = RequestMethod.POST)
	public RestApiList<Result> searchSuiteResults(@PathVariable String suiteId, @RequestBody ListModifier modifier) {
		Suite suite = suiteRepository.findOne(suiteId);
		UserHelper.checkProjectPermission(suite.getProjectId(), Permission.BASIC);
		return new RestApiList<>(resultRepository.findBySuiteAndModifier(suiteId, modifier, UserHelper.getAllowedProjectIds(Permission.BASIC)));
	}
	
	@RequestMapping(value = "/view-results/list/{viewId}", method = RequestMethod.POST)
	public RestApiList<Result> searchViewResults(@PathVariable String viewId, @RequestBody ListModifier modifier) {
		View view = viewRepository.findOne(viewId);
		UserHelper.checkViewPermission(view, Permission.BASIC);
		return new RestApiList<>(resultRepository.findByViewAndModifier(viewId, modifier, UserHelper.getAllowedProjectIds(Permission.BASIC)));
	}
	
}
