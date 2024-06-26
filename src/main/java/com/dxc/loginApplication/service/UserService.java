package com.dxc.loginApplication.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dxc.loginApplication.model.User;
import com.dxc.loginApplication.repository.UserRepository;

import jakarta.annotation.PostConstruct;



@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;

	private static Logger logger = LogManager.getLogger(UserService.class);

	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	public void persist(User user) {
		Optional<User> returnedUser = userRepo.findByUsername(user.getUsername());
		if (returnedUser.isEmpty()) {
			userRepo.save(user);
			logger.info("User successfully registered");
		} else {
			logger.warn("User already exists");
		}
	}
	
	public User findUserByUsername(String username) {
		Optional<User> returnedUser = userRepo.findByUsername(username);
		if (returnedUser.isEmpty()) {
			logger.warn("Could not find User in Database");
			return null;
		} else {
			logger.info("Returning user's details");
			return returnedUser.get();
		}
	}
	
	
	@PostConstruct
    public void init() {
		// Initialize User
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user1 = new User("user1", encoder.encode("Password1"), "ROLE_USER");
		User user2 = new User("user2", encoder.encode("Password2"), "ROLE_USER");
		User manager1 = new User("manager1", encoder.encode("Password1"), "ROLE_MANAGER");
		User manager2 = new User("manager2", encoder.encode("Password2"), "ROLE_MANAGER");
		persist(user1);
		persist(user2);
		persist(manager1);
		persist(manager2);
	}
}
