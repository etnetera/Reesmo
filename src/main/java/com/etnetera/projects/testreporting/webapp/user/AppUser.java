package com.etnetera.projects.testreporting.webapp.user;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.etnetera.projects.testreporting.webapp.model.mongodb.project.Project;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.Permission;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;
import com.etnetera.projects.testreporting.webapp.repository.mongodb.user.UserRepository;

public class AppUser implements UserDetails {

	private static Collection<? extends GrantedAuthority> authorities = Arrays.asList(new GrantedAuthority() {
		@Override
		public String getAuthority() {
			return "USER";
		}
	});
	
	@Autowired
	private transient UserRepository userRepository;
	
	private String id;
	
	private String label;
	
	private String username;
	
	private String password;
	
	private transient User user;

	public AppUser() {}
	
	public AppUser(User user) {
		setUser(user);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		if (user == null) {
			User u = userRepository.findOne(id);
			if (u == null) {
				throw new UnauthorizedException("User with id " + id + " was not found in repository.");
			}
			setUser(u);
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		id = user.getId();
		label = user.getLabel();
		username = user.getUsername();
		password = user.getPassword();
	}
	
	public void checkProjectPermission(Project project, Permission permission) {
		getUser().checkProjectPermission(project, permission);
	}
	
	public void checkProjectPermission(String projectId, Permission permission) {
		getUser().checkProjectPermission(projectId, permission);
	}
	
	public boolean isAllowedForProject(Project project, Permission permission) {
		return getUser().isAllowedForProject(project, permission);
	}
	
	public boolean isAllowedForProject(String projectId, Permission permission) {
		return getUser().isAllowedForProject(projectId, permission);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
