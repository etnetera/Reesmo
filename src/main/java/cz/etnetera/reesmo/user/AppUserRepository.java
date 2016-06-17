package cz.etnetera.reesmo.user;

import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.repository.mongodb.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
