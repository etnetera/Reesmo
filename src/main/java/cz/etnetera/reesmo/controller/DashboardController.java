package cz.etnetera.reesmo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Locale;

@Controller
public class DashboardController implements MenuActivityController {

	@Override
	public String getActiveMenu() {
		return "dashboard";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String dashboard(Model model, Locale locale) {
		return "page/dashboard";
	}
	
}
