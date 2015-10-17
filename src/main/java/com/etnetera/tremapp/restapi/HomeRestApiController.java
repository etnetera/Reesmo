package com.etnetera.tremapp.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.tremapp.Tremapp;
import com.etnetera.tremapp.user.UserHelper;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class HomeRestApiController {
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public Info info() {
		UserHelper.requireAppUser();
		return new Info(Tremapp.VERSION);
	}
	
	protected static class Info {
		
		protected String version;

		public Info(String version) {
			this.version = version;
		}
		
	}
	
}
