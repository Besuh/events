package com.besuh.events.controllers;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.besuh.events.models.User;
import com.besuh.events.services.UserService;
import com.besuh.events.validator.UserValidator;

@Controller
public class UserController {
	private UserService userService;
	private UserValidator userValidator;
	public UserController(UserService userService, UserValidator userValidator) {
		this.userService = userService;
		this.userValidator = userValidator;
		this.userService.initRoles();
	}
	@RequestMapping(value= {"/","/home"})
	public String home(Principal principal, Model model) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		userService.updateUserDate(user.getId(), user);
		model.addAttribute("currentUser", userService.findByUsername(username));
		if (user.checkIfSuper()) {
			model.addAttribute("users", userService.all());
			return "dashboard.jsp";
		}
		if (user.checkIfAdmin()) {
			// remove super from users
			List<User> subusers = userService.all();
			Iterator<User> i = subusers.iterator();
			while (i.hasNext()) {
			   User o = i.next();
			  if (o.checkIfSuper()) {
			    i.remove();
			  }
			}
			model.addAttribute("users", subusers);
			return "dashboard.jsp";
		}
		
		
		return "dashboard.jsp";
	}

	@RequestMapping("/login")
	public String login(@Valid @ModelAttribute("user") User user, @RequestParam(value="error", required=false) String error, @RequestParam(value="logout", required = false) String logout, Model model) {
		if(error != null) {
			model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
		}
		if(logout!= null) {
			model.addAttribute("logoutMessage", "Logout Successful!");
		}
		return "loginReg.jsp";
	}
	@PostMapping("/registration")
	public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		userValidator.validate(user, result);
		if (result.hasErrors()) {
			return "loginReg.jsp";
		}
		List<User> users = userService.all();
		int count = 0;
		for (User _user: users) {
			if (_user.checkIfAdmin()) {
				count++;
			}
		}
		// if no users exist, create superUser
		if (users.size() == 0) {
			userService.saveWithSuperRole(user);
		}
		// if no admins, save as admin
		else if (count == 0) {
			userService.saveUserWithAdminRole(user);
		} else {
			userService.saveWithUserRole(user);
		}
		return "redirect:/login";
	}
	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		userService.deleteUserById(id);
		return "redirect:/home";
	}
	
	@RequestMapping("/makeAdmin/{id}")
	public String makeAdmin(@PathVariable("id") Long id) {
		User user = userService.findUserById(id);
		userService.updateUserWithAdminRole(user);
		return "redirect:/home";
	}
	
	@RequestMapping("/makeUser/{id}")
	public String makeUser(@PathVariable("id") Long id) {
		User user = userService.findUserById(id);
		userService.updateWithUserRole(user);
		return "redirect:/home";
	}
}
