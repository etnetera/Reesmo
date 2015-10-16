package com.etnetera.tremapp.web.repository.mongodb.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.etnetera.tremapp.web.model.mongodb.project.Project;
import com.etnetera.tremapp.web.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.web.model.mongodb.project.ProjectGroupPermission;
import com.etnetera.tremapp.web.model.mongodb.user.ApiUser;
import com.etnetera.tremapp.web.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.web.model.mongodb.user.Permission;
import com.etnetera.tremapp.web.model.mongodb.user.User;

/**
 * User repository custom method implementation
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

	private static List<User> users = new ArrayList<>();
	
	static {
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
		
		Project p1 = new Project();
		p1.setId("tp1");
		p1.setKey("TP1");
		Project p2 = new Project();
		p2.setId("tp2");
		p2.setKey("TP2");
		
		ProjectGroup pg1 = new ProjectGroup();
		pg1.setProjects(Arrays.asList(p1, p2));
		
		ProjectGroupPermission pGP = new ProjectGroupPermission();
		pGP.setProjectGroup(pg1);
		pGP.setPermission(Permission.EDITOR);
		
		for (int i = 1; i <= 5; i++) {
			ApiUser u = new ApiUser();
			u.setId("au" + i);
			u.setLabel("API User " + i);
			u.setUsername("apiuser" + i);
			u.setPassword(passEncoder.encode("apiuser" + i));
			u.setPermissions(Arrays.asList(pGP));
			users.add(u);
		}
		
		for (int i = 1; i <= 5; i++) {
			ManualUser u = new ManualUser();
			u.setId("u" + i);
			u.setLabel("User " + i);
			u.setUsername("user" + i);
			u.setPassword(passEncoder.encode("user" + i));
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
	public User findOneByUsername(String username) {
		for (User u : users) {
			if (u.getUsername().equals(username)) return u;
		}
		return null;
	}

}
