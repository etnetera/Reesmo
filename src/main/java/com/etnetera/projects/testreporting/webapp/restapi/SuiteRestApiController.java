package com.etnetera.projects.testreporting.webapp.restapi;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.suite.Suite;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class SuiteRestApiController {
	
	@RequestMapping(value = "/suites", method = RequestMethod.POST)
	public Suite createSuite() {
		return null;
	}
	
	@RequestMapping(value = "/suites/{suiteId}", method = RequestMethod.GET)
	public Suite getSuite(@PathVariable String suiteId) {
		return null;
	}
	
	@RequestMapping(value = "/suites/{suiteId}", method = RequestMethod.POST)
	public Suite editSuite(@PathVariable String suiteId) {
		return null;
	}
	
	@RequestMapping(value = "/suites/{suiteId}", method = RequestMethod.DELETE)
	public Suite deleteSuite(@PathVariable String suiteId) {
		return null;
	}
	
	@RequestMapping(value = "/suites", method = RequestMethod.GET)
	public List<Suite> getSuites() {
		return null;
	}
	
}
