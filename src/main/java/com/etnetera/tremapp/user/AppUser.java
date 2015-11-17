package com.etnetera.tremapp.user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.etnetera.tremapp.configuration.Initializer;
import com.etnetera.tremapp.http.exception.UnauthorizedException;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;

public class AppUser implements UserDetails {
	
	private static final long serialVersionUID = -1997854508838484884L;

	@Autowired
	private transient UserRepository userRepository;
	
	private String id;
	
	private String label;
	
	private String username;
	
	private String password;
	
	private boolean enabled;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	private transient User user;

	public AppUser() {
		autowire();
	}
	
	public AppUser(User user) {
		this();
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

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public User getUser() {
		if (user == null) {
			User u = userRepository.findOneById(id);
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
		enabled = user.isEnabled();
		
		List<SimpleGrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority(user.getRole()));
		if (user.isSuperadmin()) {
			auths.add(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN));
		}
		authorities = auths;
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
	
	public void checkProjectGroupPermission(ProjectGroup projectGroup, Permission permission) {
		getUser().checkProjectGroupPermission(projectGroup, permission);
	}
	
	public void checkProjectGroupPermission(String projectGroupId, Permission permission) {
		getUser().checkProjectGroupPermission(projectGroupId, permission);
	}
	
	public boolean isAllowedForProjectGroup(ProjectGroup projectGroup, Permission permission) {
		return getUser().isAllowedForProjectGroup(projectGroup, permission);
	}
	
	public boolean isAllowedForProjectGroup(String projectGroupId, Permission permission) {
		return getUser().isAllowedForProjectGroup(projectGroupId, permission);
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
	
	public boolean hasAuthority(String authority) {
		return authority != null && authorities.stream().anyMatch(a -> a.getAuthority().equals(authority));
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		autowire();
	}
	
	private void autowire() {
		Initializer.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
	}
	
}
