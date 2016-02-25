package cz.etnetera.reesmo.controller.user;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.http.exception.ForbiddenException;
import cz.etnetera.reesmo.model.datatables.user.UserDT;
import cz.etnetera.reesmo.model.form.user.EmailCommandValidator;
import cz.etnetera.reesmo.model.form.user.PasswordCommandValidator;
import cz.etnetera.reesmo.model.form.user.UserChangePasswordCommand;
import cz.etnetera.reesmo.model.form.user.UserChangePasswordCommandValidator;
import cz.etnetera.reesmo.model.form.user.UserCommand;
import cz.etnetera.reesmo.model.form.user.UserCommandValidator;
import cz.etnetera.reesmo.model.form.user.UsernameCommandValidator;
import cz.etnetera.reesmo.model.mongodb.user.ApiUser;
import cz.etnetera.reesmo.model.mongodb.user.ManualUser;
import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.repository.mongodb.user.ManualUserRepository;
import cz.etnetera.reesmo.repository.mongodb.user.UserRepository;
import cz.etnetera.reesmo.user.UserManager;
import cz.etnetera.reesmo.user.UserRole;
import cz.etnetera.reesmo.user.UserType;

@Controller
public class UserController implements MenuActivityController {

	@Autowired
    private UserManager userManager;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ManualUserRepository manualUserRepository;

	@Override
	public String getActiveMenu() {
		return "users";
	}

	@InitBinder(value = "userCommand")
	protected void initBinder(WebDataBinder binder, @PathVariable Optional<String> userId) {		
		User user = null;
		if (userId.isPresent()) {
			user = userRepository.findOne(userId.get());
		}
		binder.addValidators(new UserCommandValidator(new UsernameCommandValidator(userRepository, user),
				new EmailCommandValidator(manualUserRepository, user), new PasswordCommandValidator(), user));
	}
	
	@InitBinder(value = "userChangePasswordCommand")
	protected void initChangePasswordBinder(WebDataBinder binder, @PathVariable String userId) {		
		User user = userRepository.findOne(userId);
		if (user != null && !userManager.isSameAsLogged(user)) {
			user = null;
		}
		binder.addValidators(new UserChangePasswordCommandValidator(new PasswordCommandValidator(), user));
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String users() {
		return "page/user/users";
	}

	@RequestMapping(value = "/dt/users")
	public @ResponseBody DatatablesResponse<UserDT> findAllForDataTables(HttpServletRequest request, Locale locale) {
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<UserDT> users = userRepository.findWithDatatablesCriterias(criterias, locale);
		return DatatablesResponse.build(users, criterias);
	}
	
	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/home/{userId}", method = RequestMethod.GET)
	public String userHome(@PathVariable String userId) {
		return "redirect:/user/detail/" + userId;
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/detail/{userId}", method = RequestMethod.GET)
	public String showUser(@PathVariable String userId, Model model) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		model.addAttribute("user", user);
		return "page/user/userDetail";
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/edit/{userId}", method = RequestMethod.GET)
	public String editUser(@PathVariable String userId, Model model) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		UserCommand userCommand = new UserCommand();
		userCommand.fromUser(user);
		model.addAttribute("user", user);
		model.addAttribute("userCommand", userCommand);
		return "page/user/userEdit";
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/edit/{userId}", method = RequestMethod.POST)
	public String editUser(@Valid UserCommand userCommand,
			BindingResult bindingResult, @PathVariable String userId, Model model) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "page/user/userEdit";
		}
		userCommand.toUser(user, false, userManager.isSameAsLogged(user));
		userRepository.save(user);
		if (userManager.isSameAsLogged(user)) {
			userManager.updateUser(user);
		}
		return "redirect:/user/detail/" + user.getId();
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/create/{type}", method = RequestMethod.GET)
	public String createUser(@PathVariable String type, Model model) {
		UserCommand userCommand = new UserCommand();
		UserType userType = UserType.fromString(type);
		if (userType == null) {
			throw new IllegalArgumentException("Unknown user type " + type);
		}
		userCommand.setType(type);
		model.addAttribute("userCommand", userCommand);
		return "page/user/userCreate";
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/create/{type}", method = RequestMethod.POST)
	public String createUser(@Valid UserCommand userCommand, BindingResult bindingResult, @PathVariable String type) {
		if (bindingResult.hasErrors()) {
			return "page/user/userCreate";
		}
		UserType userType = UserType.fromString(type);
		User user = null;
		switch (userType) {
		case MANUAL:
			user = new ManualUser();
			break;
		case API:
			user = new ApiUser();
			break;
		default:
			throw new IllegalArgumentException("Unknown user type " + userType);
		}
		userCommand.toUser(user, true, false);
		userRepository.save(user);
		return "redirect:/user/detail/" + user.getId();
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable String userId, Model model) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		if (userManager.isSameAsLogged(user)) {
			throw new ForbiddenException("You can not delete yourself!");
		}
		model.addAttribute("user", user);
		return "page/user/userDelete";
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.POST)
	public String deleteUser(@PathVariable String userId) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		if (userManager.isSameAsLogged(user)) {
			throw new ForbiddenException("You can not delete yourself!");
		}
		userRepository.delete(user);
		return "redirect:/users";
	}
	
	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/change-password/{userId}", method = RequestMethod.GET)
	public String changeUserPassword(@PathVariable String userId, Model model) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		model.addAttribute("user", user);
		model.addAttribute("userChangePasswordCommand", new UserChangePasswordCommand());
		return "page/user/userChangePassword";
	}

	@Secured({UserRole.ROLE_SUPERADMIN})
	@RequestMapping(value = "/user/change-password/{userId}", method = RequestMethod.POST)
	public String changeUserPassword(@Valid UserChangePasswordCommand userChangePasswordCommand,
			BindingResult bindingResult, @PathVariable String userId, Model model) {
		User user = userRepository.findOne(userId);
		ControllerModel.exists(user, User.class);
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			return "page/user/userChangePassword";
		}
		userChangePasswordCommand.propagateToUser(user);
		userRepository.save(user);
		if (userManager.isSameAsLogged(user)) {
			userManager.updateUser(user);
		}
		return "redirect:/user/detail/" + user.getId();
	}

}
