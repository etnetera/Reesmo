package com.etnetera.projects.testreporting.webapp.restapi;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.suite.Suite;
import com.etnetera.projects.testreporting.webapp.restapi.output.RestApiList;
import com.etnetera.projects.testreporting.webapp.utils.list.ListModifier;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class SuiteRestApiController {
	
	@RequestMapping(value = "/suites/create", method = RequestMethod.POST)
	public Suite createSuite(@RequestBody Suite suite) {
		return null;
	}
	
	@RequestMapping(value = "/suites/get/{suiteId}", method = RequestMethod.GET)
	public Suite getSuite(@PathVariable String suiteId) {
		return null;
	}
	
	@RequestMapping(value = "/suites/update/{suiteId}", method = RequestMethod.POST)
	public Suite updateSuite(@PathVariable String suiteId, @RequestBody Suite suite) {
		return null;
	}
	
	@RequestMapping(value = "/suites/{suiteId}", method = RequestMethod.DELETE)
	public Suite deleteSuite(@PathVariable String suiteId) {
		return null;
	}
	
	@RequestMapping(value = "/suites/list", method = RequestMethod.GET)
	public RestApiList<Suite> getSuites() {
		return null;
	}
	
	@RequestMapping(value = "/suites/list", method = RequestMethod.POST)
	public RestApiList<Suite> searchSuites(@RequestBody ListModifier modifier) {
		return null;
	}
	
}
