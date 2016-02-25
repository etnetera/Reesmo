package cz.etnetera.reesmo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public interface MenuActivityController {

	@ModelAttribute("activeMenu")
	public String getActiveMenu();
	
}
