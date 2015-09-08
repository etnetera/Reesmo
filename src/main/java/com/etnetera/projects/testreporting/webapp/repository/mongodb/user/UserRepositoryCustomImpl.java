package com.etnetera.projects.testreporting.webapp.repository.mongodb.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.etnetera.projects.testreporting.webapp.model.mongodb.project.Project;
import com.etnetera.projects.testreporting.webapp.model.mongodb.project.ProjectGroup;
import com.etnetera.projects.testreporting.webapp.model.mongodb.project.ProjectGroupPermission;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.ApiUser;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.ManualUser;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.Permission;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;
import com.etnetera.projects.testreporting.webapp.user.AppUser;

/**
 * User repository custom method implementation
 */
public class UserRepositoryCustomImpl implements UserRepositoryCustom, UserDetailsService {

	private static List<User> users;
	
	static {
		Project p1 = new Project();
		p1.setId("tp1");
		p1.setKey("TP1");
		Project p2 = new Project();
		p2.setId("tp2");
		p2.setKey("TP2");
		
		ProjectGroup pg1 = new ProjectGroup();
		pg1.setProjects(Arrays.asList(p1, p2));
		
		ProjectGroupPermission pGP = new ProjectGroupPermission();
		pGP.setPermission(Permission.EDITOR);
		
		for (int i = 1; i <= 5; i++) {
			ApiUser u = new ApiUser();
			u.setId("au" + i);
			u.setLabel("API User " + i);
			u.setUsername("apiuser" + i);
			u.setPassword("apiuser" + i);
			u.setPermissions(Arrays.asList(pGP));
			users.add(u);
		}
		
		for (int i = 1; i <= 5; i++) {
			ManualUser u = new ManualUser();
			u.setId("u" + i);
			u.setLabel("User " + i);
			u.setUsername("user" + i);
			u.setPassword("user" + i);
			u.setPermissions(Arrays.asList(pGP));
			users.add(u);
		}
	}
	
	@Override
	public User findOne(String id) {
		for (User u : users) {
			if (u.getId().equals(id)) return u;
		}
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		for (User u : users) {
			if (u.getUsername().equals(username)) {
				return new AppUser(u);
			}
		}
		return null;
	}

}
