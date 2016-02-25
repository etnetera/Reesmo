package cz.etnetera.reesmo.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cz.etnetera.reesmo.Reesmo;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class HomeRestApiController extends AbstractRestController {
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public Info info() {
		return new Info(Reesmo.VERSION);
	}
	
	protected static class Info {
		
		protected String version;

		public Info(String version) {
			this.version = version;
		}
		
	}
	
}
