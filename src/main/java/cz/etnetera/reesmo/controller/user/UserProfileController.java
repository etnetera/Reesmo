package cz.etnetera.reesmo.controller.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.etnetera.reesmo.model.form.user.EmailCommandValidator;
import cz.etnetera.reesmo.model.form.user.PasswordCommandValidator;
import cz.etnetera.reesmo.model.form.user.UserChangePasswordCommand;
import cz.etnetera.reesmo.model.form.user.UserChangePasswordCommandValidator;
import cz.etnetera.reesmo.model.form.user.UserProfileCommand;
import cz.etnetera.reesmo.model.form.user.UserProfileCommandValidator;
import cz.etnetera.reesmo.model.form.user.UsernameCommandValidator;
import cz.etnetera.reesmo.model.mongodb.user.ManualUser;
import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.repository.mongodb.user.ManualUserRepository;
import cz.etnetera.reesmo.repository.mongodb.user.UserRepository;
import cz.etnetera.reesmo.user.UserManager;

@Controller
public class UserProfileController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ManualUserRepository manualUserRepository;
	
	@InitBinder(value = "userProfileCommand")
	protected void initBinder(WebDataBinder binder) {		
		User user = userManager.requireUser();
		binder.addValidators(new UserProfileCommandValidator(new UsernameCommandValidator(userRepository, user),
				new EmailCommandValidator(manualUserRepository, user)));
	}
	
	@InitBinder(value = "userChangePasswordCommand")
	protected void initChangePasswordBinder(WebDataBinder binder) {		
		User user = userManager.requireUser();
		binder.addValidators(new UserChangePasswordCommandValidator(new PasswordCommandValidator(), user));
	}
	
	@RequestMapping(value = "/user-profile", method = RequestMethod.GET)
	public String userProfile(Model model) {
		model.addAttribute("user", userManager.requireUser());
		return "page/user/userProfile";
	}
	
	@RequestMapping(value = "/user-profile/edit", method = RequestMethod.GET)
	public String editUserProfile(Model model) {
		User user = userManager.requireUser();
		UserProfileCommand userProfileCommand = new UserProfileCommand();
		userProfileCommand.fromUser((ManualUser) user);
		model.addAttribute("user", user);
		model.addAttribute("userProfileCommand", userProfileCommand);
		return "page/user/userProfileEdit";
	}
	
	@RequestMapping(value = "/user-profile/edit", method = RequestMethod.POST)
	public String editUserProfile(@Valid UserProfileCommand userProfileCommand, BindingResult bindingResult, Model model) {
		User user = userManager.requireUser();
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "page/user/userProfileEdit";
		}
		userProfileCommand.toUser((ManualUser) user);
		userRepository.save(user);
		userManager.updateUser(user);
		return "redirect:/user-profile";
	}
	
	@RequestMapping(value = "/user-profile/change-password", method = RequestMethod.GET)
	public String changeUserProfilePassword(Model model) {
		User user = userManager.requireUser();
		model.addAttribute("user", user);
		model.addAttribute("userChangePasswordCommand", new UserChangePasswordCommand());
		return "page/user/userProfileChangePassword";
	}

	@RequestMapping(value = "/user-profile/change-password", method = RequestMethod.POST)
	public String changeUserProfilePassword(@Valid UserChangePasswordCommand userChangePasswordCommand,
			BindingResult bindingResult, Model model) {
		User user = userManager.requireUser();
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "page/user/userProfileChangePassword";
		}
		userChangePasswordCommand.propagateToUser(user);
		userRepository.save(user);
		userManager.updateUser(user);
		return "redirect:/user-profile";
	}

}
