package com.etnetera.tremapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.http.exception.NotFoundException;
import com.etnetera.tremapp.model.datatables.UserDT;
import com.etnetera.tremapp.model.form.user.ApiUserCommand;
import com.etnetera.tremapp.model.form.user.ApiUserCreateCommand;
import com.etnetera.tremapp.model.form.user.ManualUserCommand;
import com.etnetera.tremapp.model.form.user.ManualUserCreateCommand;
import com.etnetera.tremapp.model.form.user.UserCommand;
import com.etnetera.tremapp.model.form.user.UserCreateCommand;
import com.etnetera.tremapp.model.mongodb.user.ApiUser;
import com.etnetera.tremapp.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.user.ApiUserRepository;
import com.etnetera.tremapp.repository.mongodb.user.ManualUserRepository;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserHelper;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class UserController implements MenuActivityController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ManualUserRepository manualUserRepository;
	
	@Autowired
	private ApiUserRepository apiUserRepository;
	
	@Override
	public String getActiveMenu() {
		return "users";
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String users() {
		return "page/user/users";
	}

	@RequestMapping(value = "/dt/users")
	public @ResponseBody DatatablesResponse<UserDT> findAllForDataTables(HttpServletRequest request) {
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<UserDT> users = userRepository.findWithDatatablesCriterias(criterias);
		return DatatablesResponse.build(users, criterias);
	}
	
	@RequestMapping(value = "/users/detail/{userId}", method = RequestMethod.GET)
	public String showUser(@PathVariable String userId, Model model) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			throw new NotFoundException();
		}
		model.addAttribute("user", user);
		return "page/user/userDetail";
	}
	
	@RequestMapping(value = "/user-profile", method = RequestMethod.GET)
	public String userProfile(Model model) {
		model.addAttribute("user", UserHelper.requireUser());
		model.addAttribute("activeMenu", "");
		return "page/user/userProfile";
	}
	
	@RequestMapping(value = "/users/create/manual", method = RequestMethod.GET)
	public String createManualUser(ManualUserCreateCommand userCommand) {
		return "page/user/userCreateManual";
	}
	
	@RequestMapping(value = "/users/create/manual", method = RequestMethod.POST)
	public String createManualUser(@Valid ManualUserCreateCommand userCommand, BindingResult bindingResults) {
		if (bindingResults.hasErrors()) {
			return "page/user/userCreateManual";
		}
		ManualUser user = new ManualUser();
		pushCommand(user, userCommand);
		manualUserRepository.save(user);
		return "redirect:/users/detail/" + user.getId();
	}
	
	@RequestMapping(value = "/users/create/api", method = RequestMethod.GET)
	public String createApiUser(ApiUserCreateCommand userCommand) {
		return "page/user/userCreateApi";
	}
	
	@RequestMapping(value = "/users/create/api", method = RequestMethod.POST)
	public String createApiUser(@Valid ApiUserCreateCommand userCommand, BindingResult bindingResults) {
		if (bindingResults.hasErrors()) {
			return "page/user/userCreateApi";
		}
		ApiUser user = new ApiUser();
		pushCommand(user, userCommand);
		apiUserRepository.save(user);
		return "redirect:/users/detail/" + user.getId();
	}
	
	private void pushCommand(User user, UserCommand userCommand) {
		user.setLabel(userCommand.getLabel());
		user.setUsername(userCommand.getUsername());
		user.setActive(userCommand.isActive());
		user.setSuperadmin(userCommand.isSuperadmin());
	}
	
	private void pushCommand(User user, UserCreateCommand userCommand) {
		pushCommand(user, (UserCommand) userCommand); 
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
		user.setPassword(passEncoder.encode(userCommand.getPassword()));
	}
	
	private void pushCommand(ManualUser user, ManualUserCommand userCommand) {
		pushCommand(user, (UserCommand) userCommand); 
		user.setEmail(userCommand.getEmail());
	}
	
	private void pushCommand(ManualUser user, ManualUserCreateCommand userCommand) {
		pushCommand(user, (UserCreateCommand) userCommand); 
		user.setEmail(userCommand.getEmail());
	}
	
	private void pushCommand(ApiUser user, ApiUserCommand userCommand) {
		pushCommand(user, (UserCommand) userCommand); 
		user.setAllowedIps(userCommand.getAllowedIps());
	}
	
	private void pushCommand(ApiUser user, ApiUserCreateCommand userCommand) {
		pushCommand(user, (UserCreateCommand) userCommand); 
		user.setAllowedIps(userCommand.getAllowedIps());
	}

}
