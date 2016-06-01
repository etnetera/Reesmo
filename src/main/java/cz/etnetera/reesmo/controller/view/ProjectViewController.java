package cz.etnetera.reesmo.controller.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.swing.ListModel;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.datatables.filter.FilteredDatatablesCriterias;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.http.exception.ValidationErrorException;
import cz.etnetera.reesmo.list.ListModifier;
import cz.etnetera.reesmo.list.filter.ListFilter;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.result.ResultDT;
import cz.etnetera.reesmo.model.form.project.ProjectCommand;
import cz.etnetera.reesmo.model.form.project.ProjectCommandValidator;
import cz.etnetera.reesmo.model.form.view.ViewCommand;
import cz.etnetera.reesmo.model.form.view.ViewCommandValidator;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.elasticsearch.result.ResultRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import cz.etnetera.reesmo.user.UserManager;

@Controller
public class ProjectViewController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ViewRepository viewRepository;
	
	@Autowired
	private Localizer localizer;
	
	@Override
	public String getActiveMenu() {
		return "projectViews";
	}
	
	@InitBinder(value = "viewCommand")
	protected void initBinder(WebDataBinder binder) {		
		binder.addValidators(new ViewCommandValidator());
	}
	
	@RequestMapping(value = "/project/results/{projectId}", method = RequestMethod.GET)
	public String results(@PathVariable String projectId, Model model, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("project", project);
		return "page/project/projectResults";
	}

	@RequestMapping(value = "/project/view/create/{projectId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody View createProjectView(@Valid ViewCommand viewCommand,
			BindingResult bindingResult, @PathVariable String projectId, HttpServletRequest request, Locale locale) throws Exception {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.EDITOR);
		
		if (bindingResult.hasErrors()) {
			throw new ValidationErrorException(bindingResult.getAllErrors().stream().map(er -> er.toString()).collect(Collectors.joining(". ")));
		}
		View view = new View();
		viewCommand.toView(view);
		
		List<ListFilter> filters = FilteredDatatablesCriterias.getFiltersFromRequest(request);
		if (filters == null) {
			throw new ValidationErrorException("Filters are empty or not well-formed.");
		}
		ListModifier modifier = new ListModifier();
		modifier.setFilters(filters);
		view.setModifier(modifier);
		viewRepository.save(view);
		return view;
	}

}
