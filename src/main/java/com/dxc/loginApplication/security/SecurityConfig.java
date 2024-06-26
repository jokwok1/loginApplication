package com.dxc.loginApplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	AuthenticationSuccessHandlerSecurity authenticationSuccessHandlerSecurity;
	
	/**
     * Creates a BCryptPasswordEncoder bean.
     *
     * @return A BCryptPasswordEncoder bean.
     */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 /**
     * Creates a UserDetailsService bean.
     *
     * @return A UserDetailsService bean.
     */
	@Bean
	public UserDetailsService getDetailsService() {
		return new UserSecurityDetailsService();
	}
	
	/**
     * Creates a DaoAuthenticationProvider bean.
     *
     * @return A DaoAuthenticationProvider bean.
     */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	/**
     * Creates a SecurityFilterChain bean.
     *
     * @param http The HttpSecurity object.
     * @return A SecurityFilterChain bean.
     * @throws Exception If an error occurs.
     */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/index", "/register", "/login", "/*.css", "/*.png", "/*.js", "/*.jpeg")
				.permitAll().requestMatchers("/manager/**").hasRole("MANAGER")
				.requestMatchers("/**").hasAnyRole("USER", "MANAGER")
				.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").failureUrl("/login?error=true")
						.successHandler(authenticationSuccessHandlerSecurity))
				.logout(logout -> logout.logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true)
						.clearAuthentication(true).permitAll());

		return http.build();
	}
}
