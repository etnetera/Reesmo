package com.etnetera.tremapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.etnetera.tremapp.model.form.user.UserProfileCommand;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserHelper;

@Controller
public class UserProfileController {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value = "/user-profile", method = RequestMethod.GET)
	public String userProfile(Model model) {
		model.addAttribute("user", UserHelper.requireUser());
		return "page/user/userProfile";
	}
	
	@RequestMapping(value = "/user-profile/edit", method = RequestMethod.GET)
	public String editUserProfile(UserProfileCommand userCommand, Model model) {
		User user = UserHelper.requireUser();
		model.addAttribute("user", user);
		userCommand.updateFromUser(user);
		return "page/user/" + user.getType().name().toLowerCase() + "UserProfileEdit";
	}
	
	@RequestMapping(value = "/user-profile/edit", method = RequestMethod.POST)
	public String editUser(@Valid UserProfileCommand userCommand, Model model, BindingResult bindingResult) {
		User user = UserHelper.requireUser();
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "page/user/" + user.getType().name().toLowerCase() + "UserProfileEdit";
		}
		userCommand.propagateToUser(user);
		userRepository.save(user);
		return "redirect:/user-profile";
	}

}
