package com.etnetera.projects.testreporting.webapp.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;
import com.etnetera.projects.testreporting.webapp.repository.elasticsearch.result.ResultRepository;
import com.etnetera.projects.testreporting.webapp.restapi.output.RestApiList;
import com.etnetera.projects.testreporting.webapp.utils.list.ListModifier;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class ResultRestApiController {
	
	@Autowired
	private ResultRepository resultRepository;
	
	@RequestMapping(value = "/results/create", method = RequestMethod.POST)
	public Result createResult(@RequestBody Result result) {
		return resultRepository.save(result);
	}
	
	@RequestMapping(value = "/results/get/{resultId}", method = RequestMethod.GET)
	public Result getResult(@PathVariable String resultId) {
		return resultRepository.findOne(resultId);
	}
	
	@RequestMapping(value = "/results/update/{resultId}", method = RequestMethod.POST)
	public Result updateResult(@PathVariable String resultId, @RequestBody Result result) {
		result.setId(resultId);
		return resultRepository.save(result);
	}
	
	@RequestMapping(value = "/results/delete/{resultId}", method = RequestMethod.DELETE)
	public Result deleteResult(@PathVariable String resultId) {
		Result result = resultRepository.findOne(resultId);
		resultRepository.delete(result);
		return result;
	}
	
	@RequestMapping(value = "/results/list", method = RequestMethod.GET)
	public RestApiList<Result> getResults() {
		return new RestApiList<>(resultRepository.findByQueryWrapper(new ListModifier()));
	}
	
	@RequestMapping(value = "/suite-results/list/{suiteId}", method = RequestMethod.GET)
	public RestApiList<Result> getSuiteResults(@PathVariable String suiteId) {
		return new RestApiList<>(resultRepository.findBySuiteAndQueryWrapper(suiteId, new ListModifier()));
	}
	
	@RequestMapping(value = "/view-results/list/{viewId}", method = RequestMethod.GET)
	public RestApiList<Result> getViewResults(@PathVariable String viewId) {
		return new RestApiList<>(resultRepository.findByViewAndQueryWrapper(viewId, new ListModifier()));
	}
	
	@RequestMapping(value = "/results/list", method = RequestMethod.POST)
	public RestApiList<Result> searchResults(@RequestBody ListModifier modifier) {
		return new RestApiList<>(resultRepository.findByQueryWrapper(modifier));
	}
	
	@RequestMapping(value = "/suite-results/list/{suiteId}", method = RequestMethod.POST)
	public RestApiList<Result> searchSuiteResults(@PathVariable String suiteId, @RequestBody ListModifier modifier) {
		return new RestApiList<>(resultRepository.findBySuiteAndQueryWrapper(suiteId, modifier));
	}
	
	@RequestMapping(value = "/view-results/list/{viewId}", method = RequestMethod.POST)
	public RestApiList<Result> searchViewResults(@PathVariable String viewId, @RequestBody ListModifier modifier) {
		return new RestApiList<>(resultRepository.findByViewAndQueryWrapper(viewId, modifier));
	}
	
}
