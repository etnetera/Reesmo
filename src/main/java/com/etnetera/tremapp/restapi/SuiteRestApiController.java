package com.etnetera.tremapp.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.tremapp.model.elasticsearch.suite.Suite;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.elasticsearch.suite.SuiteRepository;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.restapi.output.RestApiList;
import com.etnetera.tremapp.user.UserHelper;
import com.etnetera.tremapp.utils.list.ListModifier;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class SuiteRestApiController {
	
	@Autowired
	private SuiteRepository suiteRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@RequestMapping(value = "/suites/create", method = RequestMethod.POST)
	public Suite createSuite(@RequestBody Suite suite) {
		UserHelper.checkProjectPermission(suite.getProjectId(), Permission.EDITOR);
		return suiteRepository.save(suite);
	}
	
	@RequestMapping(value = "/suites/create/{projectKey}", method = RequestMethod.POST)
	public Suite createSuite(@PathVariable String projectKey, @RequestBody Suite suite) {
		Project project = projectRepository.findOneByKey(projectKey);
		UserHelper.checkProjectPermission(project.getId(), Permission.EDITOR);
		suite.setProjectId(project.getId());
		return suiteRepository.save(suite);
	}
	
	@RequestMapping(value = "/suites/get/{suiteId}", method = RequestMethod.GET)
	public Suite getSuite(@PathVariable String suiteId) {
		Suite suite = suiteRepository.findOne(suiteId);
		UserHelper.checkProjectPermission(suite.getProjectId(), Permission.BASIC);
		return suite;
	}
	
	@RequestMapping(value = "/suites/update/{suiteId}", method = RequestMethod.POST)
	public Suite updateSuite(@PathVariable String suiteId, @RequestBody Suite suite) {
		Suite persistedSuite = suiteRepository.findOne(suiteId);
		UserHelper.checkProjectPermission(persistedSuite.getProjectId(), Permission.EDITOR);
		if (suite.getProjectId() == null)
			suite.setProjectId(persistedSuite.getProjectId());
		else if (!suite.getProjectId().equals(persistedSuite.getProjectId()))
			UserHelper.checkProjectPermission(suite.getProjectId(), Permission.EDITOR);
		suite.setId(suiteId);
		return suiteRepository.save(suite);
	}
	
	@RequestMapping(value = "/suites/delete/{suiteId}", method = RequestMethod.GET)
	public void deleteSuite(@PathVariable String suiteId) {
		Suite suite = suiteRepository.findOne(suiteId);
		UserHelper.checkProjectPermission(suite.getProjectId(), Permission.EDITOR);
		suiteRepository.delete(suite);
	}
	
	@RequestMapping(value = "/suites/list", method = RequestMethod.GET)
	public RestApiList<Suite> getSuites() {
		return new RestApiList<>(suiteRepository.findByModifier(new ListModifier(), UserHelper.getAllowedProjectIds(Permission.BASIC)));
	}
	
	@RequestMapping(value = "/suites/list", method = RequestMethod.POST)
	public RestApiList<Suite> searchSuites(@RequestBody ListModifier modifier) {
		return new RestApiList<>(suiteRepository.findByModifier(modifier, UserHelper.getAllowedProjectIds(Permission.BASIC)));
	}
	
}
