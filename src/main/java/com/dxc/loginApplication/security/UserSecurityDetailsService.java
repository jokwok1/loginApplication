package com.dxc.loginApplication.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dxc.loginApplication.model.User;
import com.dxc.loginApplication.repository.UserRepository;

@Service
public class UserSecurityDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;

    /**
     * Loads a user by their username.
     *
     * @param username The username of the user to load.
     * @return A UserDetails object containing the user's details.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username + " not found");
        } else if (!user.get().getUsername().equals(username)){
        	throw new UsernameNotFoundException(username + " not found");
        } else {
        	return new UserSecurityDetails(user.get());
        }
    }
}
