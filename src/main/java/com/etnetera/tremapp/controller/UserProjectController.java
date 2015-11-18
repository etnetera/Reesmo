package com.etnetera.tremapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.http.ControllerModel;
import com.etnetera.tremapp.model.datatables.user.UserProjectDT;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserManager;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class UserProjectController {

	@Autowired
    private UserManager userManager;

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/dt/users/projects/{userId}")
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
