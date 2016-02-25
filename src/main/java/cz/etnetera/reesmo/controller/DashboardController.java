package cz.etnetera.reesmo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DashboardController implements MenuActivityController {

	@Override
	public String getActiveMenu() {
		return "dashboard";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String dashboard(Model model) {
		return "page/dashboard";
	}
	
}
