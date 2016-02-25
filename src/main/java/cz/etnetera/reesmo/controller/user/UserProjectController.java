package cz.etnetera.reesmo.controller.user;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.model.datatables.user.UserProjectDT;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.user.UserRepository;
import cz.etnetera.reesmo.user.UserManager;

@Controller
public class UserProjectController {

	@Autowired
    private UserManager userManager;

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/dt/user/projects/{userId}")
	public @ResponseBody DatatablesResponse<UserProjectDT> findAllForDataTables(@PathVariable String userId,
			HttpServletRequest request, Locale locale) {
		// use logged user on id match
		User user = userManager.requireUserId().equals(userId) ? userManager.requireUser() : userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		user.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<UserProjectDT> projects = projectRepository.findUserProjectsWithDatatablesCriterias(criterias, user, locale);
		return DatatablesResponse.build(projects, criterias);
	}

}
