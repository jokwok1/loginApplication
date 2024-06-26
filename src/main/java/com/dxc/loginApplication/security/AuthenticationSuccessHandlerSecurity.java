package com.dxc.loginApplication.security;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dxc.loginApplication.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationSuccessHandlerSecurity implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private static Logger logger = LogManager.getLogger(AuthenticationSuccessHandlerSecurity.class);

	@Autowired
	HttpSession session;

	@Autowired
	UserService userService;

	/**
	 * Handles successful user authentication by setting session attributes and redirecting users based on their role.
	 * <p>
	 * This method is invoked upon successful authentication of a user. It retrieves the authenticated user details, sets them as a session attribute, and redirects the user to the appropriate dashboard based on their role (admin or regular user).
	 *
	 * @param request        The HttpServletRequest object representing the user's request.
	 * @param response       The HttpServletResponse object representing the response to be sent to the user.
	 * @param authentication The Authentication object containing authentication information.
	 * @throws IOException      if an I/O error occurs during redirection.
	 * @throws ServletException if the request cannot be handled.
	 * @see org.example.AuthenticationSuccessHandlerSecurity
	 * @see org.example.UserSecurityDetails
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserSecurityDetails loggedUser = (UserSecurityDetails) authentication.getPrincipal();
		session.setAttribute("loggedUser", loggedUser.getUser());
		
		if (loggedUser.getUser().getRole().equals("ROLE_MANAGER")) {
			logger.info("Manager has logged in");
			redirectStrategy.sendRedirect(request, response, "/welcome");
		} else {
			logger.info("User has logged in");
			redirectStrategy.sendRedirect(request, response, "/welcome");
		}
	}

}