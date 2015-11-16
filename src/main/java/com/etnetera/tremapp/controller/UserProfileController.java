package com.etnetera.tremapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.etnetera.tremapp.model.form.user.EmailCommandValidator;
import com.etnetera.tremapp.model.form.user.PasswordCommandValidator;
import com.etnetera.tremapp.model.form.user.UserChangePasswordCommand;
import com.etnetera.tremapp.model.form.user.UserChangePasswordCommandValidator;
import com.etnetera.tremapp.model.form.user.UserProfileCommand;
import com.etnetera.tremapp.model.form.user.UserProfileCommandValidator;
import com.etnetera.tremapp.model.form.user.UsernameCommandValidator;
import com.etnetera.tremapp.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.user.ManualUserRepository;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserHelper;

@Controller
public class UserProfileController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ManualUserRepository manualUserRepository;
	
	@InitBinder(value = "userProfileCommand")
	protected void initBinder(WebDataBinder binder) {		
		User user = UserHelper.requireUser();
		binder.addValidators(new UserProfileCommandValidator(new UsernameCommandValidator(userRepository, user),
				new EmailCommandValidator(manualUserRepository, user)));
	}
	
	@InitBinder(value = "userChangePasswordCommand")
	protected void initChangePasswordBinder(WebDataBinder binder) {		
		User user = UserHelper.requireUser();
		binder.addValidators(new UserChangePasswordCommandValidator(new PasswordCommandValidator(), user));
	}
	
	@RequestMapping(value = "/user-profile", method = RequestMethod.GET)
	public String userProfile(Model model) {
		model.addAttribute("user", UserHelper.requireUser());
		return "page/user/userProfile";
	}
	
	@RequestMapping(value = "/user-profile/edit", method = RequestMethod.GET)
	public String editUserProfile(Model model) {
		User user = UserHelper.requireUser();
		UserProfileCommand userProfileCommand = new UserProfileCommand();
		userProfileCommand.updateFromUser((ManualUser) user);
		model.addAttribute("user", user);
		model.addAttribute("userProfileCommand", userProfileCommand);
		return "page/user/userProfileEdit";
	}
	
	@RequestMapping(value = "/user-profile/edit", method = RequestMethod.POST)
	public String editUserProfile(@Valid UserProfileCommand userProfileCommand, BindingResult bindingResult, Model model) {
		User user = UserHelper.requireUser();
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "page/user/userProfileEdit";
		}
		userProfileCommand.propagateToUser((ManualUser) user);
		userRepository.save(user);
		UserHelper.updateUser(user);
		return "redirect:/user-profile";
	}
	
	@RequestMapping(value = "/user-profile/change-password", method = RequestMethod.GET)
	public String changeUserProfilePassword(Model model) {
		User user = UserHelper.requireUser();
		model.addAttribute("user", user);
		model.addAttribute("userChangePasswordCommand", new UserChangePasswordCommand());
		return "page/user/userProfileChangePassword";
	}

	@RequestMapping(value = "/user-profile/change-password", method = RequestMethod.POST)
	public String changeUserProfilePassword(@Valid UserChangePasswordCommand userChangePasswordCommand,
			BindingResult bindingResult, Model model) {
		User user = UserHelper.requireUser();
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "page/user/userProfileChangePassword";
		}
		userChangePasswordCommand.propagateToUser(user);
		userRepository.save(user);
		UserHelper.updateUser(user);
		return "redirect:/user-profile";
	}

}
