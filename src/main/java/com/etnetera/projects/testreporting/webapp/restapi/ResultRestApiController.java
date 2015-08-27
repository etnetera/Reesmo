package com.etnetera.projects.testreporting.webapp.restapi;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class ResultRestApiController {
	
	@RequestMapping(value = "/results", method = RequestMethod.POST)
	public Result createResult(@RequestBody Result result) {
		return null;
	}
	
	@RequestMapping(value = "/results/{resultId}", method = RequestMethod.GET)
	public Result getResult(@PathVariable String resultId) {
		return null;
	}
	
	@RequestMapping(value = "/results/{resultId}", method = RequestMethod.DELETE)
	public Result deleteResult(@PathVariable String resultId) {
		return null;
	}
	
	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public List<Result> getResults() {
		return null;
	}
	
	@RequestMapping(value = "/suite-results/{suiteId}", method = RequestMethod.GET)
	public List<Result> getSuiteResults(@PathVariable String suiteId) {
		return null;
	}
	
	@RequestMapping(value = "/view-results/{viewId}", method = RequestMethod.GET)
	public List<Result> getViewResults(@PathVariable String viewId) {
		return null;
	}
	
}
