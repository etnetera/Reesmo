package cz.etnetera.reesmo.controller.project;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.model.datatables.project.ProjectDT;
import cz.etnetera.reesmo.model.form.project.ProjectCommand;
import cz.etnetera.reesmo.model.form.project.ProjectCommandValidator;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class ProjectsController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Override
	public String getActiveMenu() {
		return "projects";
	}
	
	@InitBinder(value = "projectCommand")
	protected void initBinder(WebDataBinder binder, @PathVariable Optional<String> projectId) {		
		Project project = null;
		if (projectId.isPresent()) {
			project = projectRepository.findOne(projectId.get());
		}
		binder.addValidators(new ProjectCommandValidator(projectRepository, project));
	}
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public String projects() {
		return "page/project/projects";
	}

	@RequestMapping(value = "/dt/projects")
	public @ResponseBody DatatablesResponse<ProjectDT> findAllForDataTables(@RequestParam(required = false, defaultValue = "BASIC") String permission, HttpServletRequest request) {
		Permission perm = Permission.fromString(permission);
		if (perm == null) {
			throw new IllegalArgumentException("Unknown permission " + permission);
		}
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectDT> projects = projectRepository.findWithDatatablesCriterias(criterias, userManager.getAllowedProjectIds(perm));
		return DatatablesResponse.build(projects, criterias);
	}

	@RequestMapping(value = "/project/create", method = RequestMethod.GET)
	public String createProject(Model model) {
		ProjectCommand projectCommand = new ProjectCommand();
		model.addAttribute("projectCommand", projectCommand);
		return "page/project/projectCreate";
	}

	@RequestMapping(value = "/project/create", method = RequestMethod.POST)
	public String createProject(@Valid ProjectCommand projectCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "page/project/projectCreate";
		}
		Project project = new Project();
		projectCommand.toProject(project);
		project.getUsers().put(userManager.requireUserId(), Permission.OWNER);
		projectRepository.save(project);
		userManager.saveUserProjectsPermissions(userManager.requireUser());
		return "redirect:/project/detail/" + project.getId();
	}

}
