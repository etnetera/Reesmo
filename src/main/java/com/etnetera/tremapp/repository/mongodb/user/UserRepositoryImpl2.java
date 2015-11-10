package com.etnetera.tremapp.repository.mongodb.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.etnetera.tremapp.model.mongodb.user.ApiUser;
import com.etnetera.tremapp.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;

/**
 * User repository custom method implementation
 */
public class UserRepositoryImpl2 implements UserRepositoryCustom2 {

	@Autowired
	private ProjectRepository projectRepository;
	
	private List<User> users;

	@Override
	public List<User> findAll() {
		return getUsers();
	}
	
	@Override
	public User findOneById(String id) {
		for (User u : getUsers()) {
			if (u.getId().equals(id)) return u;
		}
		return null;
	}

	@Override
	public User findOneByUsername(String username) {
		for (User u : getUsers()) {
			if (u.getUsername().equals(username)) return u;
		}
		return null;
	}
	
	private List<User> getUsers() {
		if (this.users == null) {
			List<User> users = new ArrayList<>();
			BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
			
			Map<String, Permission> projectsPermissions = new HashMap<>();
			projectRepository.findAll().forEach(p -> {
				projectsPermissions.put(p.getId(), Permission.EDITOR);
			});
			
			for (int i = 1; i <= 5; i++) {
				ApiUser u = new ApiUser();
				u.setId("au" + i);
				u.setLabel("API User " + i);
				u.setUsername("apiuser" + i);
				u.setPassword(passEncoder.encode("apiuser" + i));
				u.setProjectsPermissions(projectsPermissions);
				users.add(u);
			}
			
			for (int i = 1; i <= 5; i++) {
				ManualUser u = new ManualUser();
				u.setId("u" + i);
				u.setLabel("User " + i);
				u.setUsername("user" + i);
				u.setPassword(passEncoder.encode("user" + i));
				u.setProjectsPermissions(projectsPermissions);
				users.add(u);
			}
			
			this.users = users;
		}
		return this.users;
	}

}
