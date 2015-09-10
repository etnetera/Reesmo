package com.etnetera.projects.testreporting.webapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;
import com.etnetera.projects.testreporting.webapp.repository.mongodb.user.UserRepository;

public class AppUserRepository implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findOneByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		return new AppUser(user);
	}

}
