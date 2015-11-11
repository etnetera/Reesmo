package com.etnetera.tremapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.http.ControllerModel;
import com.etnetera.tremapp.model.datatables.UserDT;
import com.etnetera.tremapp.model.form.user.UserCommand;
import com.etnetera.tremapp.model.mongodb.user.ApiUser;
import com.etnetera.tremapp.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserRole;
import com.etnetera.tremapp.user.UserType;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Secured({UserRole.ROLE_ADMIN})
@Controller
public class UserController implements MenuActivityController {
	
	@Autowired
	private UserRepository userRepository;
	
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
		ControllerModel.exists(user, User.class);
		model.addAttribute("user", user);
		return "page/user/userDetail";
	}
	
	@RequestMapping(value = "/users/edit/{userId}", method = RequestMethod.GET)
	public String editUser(@PathVariable String userId, UserCommand userCommand, Model model) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		userCommand.updateFromUser(user);
		model.addAttribute("user", user);
		return "page/user/" + user.getType().name().toLowerCase() + "UserEdit";
	}
	
	@RequestMapping(value = "/users/edit/{userId}", method = RequestMethod.POST)
	public String editUser(@PathVariable String userId, @Valid UserCommand userCommand, Model model, BindingResult bindingResult) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "page/user/" + user.getType().name().toLowerCase() + "UserEdit";
		}
		userCommand.propagateToUser(user);
		userRepository.save(user);
		return "redirect:/users/detail/" + user.getId();
	}
	
	@RequestMapping(value = "/users/create/{type}", method = RequestMethod.GET)
	public String createUser(@PathVariable String type, UserCommand userCommand) {
		UserType userType = UserType.fromString(type);
		if (userType == null) {
			throw new IllegalArgumentException("Uknown user type " + type);
		}
		userCommand.setType(userType);
		return "page/user/" + userCommand.getType().name().toLowerCase() + "UserCreate";
	}
	
	@RequestMapping(value = "/users/create/{type}", method = RequestMethod.POST)
	public String createUser(@PathVariable String type, @Valid UserCommand userCommand, BindingResult bindingResult) {
		UserType userType = UserType.fromString(type);
		if (userType == null) {
			throw new IllegalArgumentException("Uknown user type " + type);
		}
		if (bindingResult.hasErrors()) {
			userCommand.setType(userType);
			return "page/user/" + userCommand.getType().name().toLowerCase() + "UserCreate";
		}
		User user = null;
		switch (userType) {
		case MANUAL:
			user = new ManualUser();
			break;
		case API:
			user = new ApiUser();
			break;
		default:
			throw new IllegalArgumentException("Uknown user type " + userType);
		}
		userCommand.propagateToUser(user);
		userRepository.save(user);
		return "redirect:/users/detail/" + user.getId();
	}
	
	@RequestMapping(value = "/users/delete/{userId}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable String userId, Model model) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		model.addAttribute("user", user);
		return "page/user/userDelete";
	}
	
	@RequestMapping(value = "/users/delete/{userId}", method = RequestMethod.POST)
	public String deleteUser(@PathVariable String userId) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		userRepository.delete(user);
		return "redirect:/users";
	}

}
