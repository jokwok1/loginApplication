package com.dxc.loginApplication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dxc.loginApplication.model.User;
import com.dxc.loginApplication.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * Manages View of webpage
 *
 * @return The name of the view to be rendered.
 */
@Controller
public class UserController {
	@Autowired
	private UserService userService;
	private static Logger logger = LogManager.getLogger(UserController.class);
	
	@GetMapping("/index")
	public String indexPage() {
		return "index";
	}	

    /**
     * This method displays the login page.
     *
     * @return The name of the view to be rendered.
     */
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	
	
	@GetMapping("/welcome")
    public String welcome(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", returnedUser);
        return "welcome";
    }
	
    /**
     * This method logs out the user and redirects to the login page.
     *
     * @param session The HTTP session containing the logged-in user information.
     * @return The name of the view to be rendered.
     */
	@GetMapping("/logout")
	public String logoutPage(HttpSession session) {
		logger.info("User has logged out.");
		session.invalidate();
		return "redirect:/login";
	}
	
	/**
     * Redirects to manager only restricted view
     *
     * @param session The HTTP session containing the logged-in user information.
     * @return The name of the view to be rendered.
     */
	@GetMapping("/manager/restricted")
	public String restrictedPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", returnedUser);
		
		return "manager/restricted";
	}
}
