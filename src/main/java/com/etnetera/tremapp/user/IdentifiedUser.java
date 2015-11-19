package com.etnetera.tremapp.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.etnetera.tremapp.model.mongodb.user.User;

public interface IdentifiedUser {
	
	public String getId();

	public String getLabel();
	
	public String getUsername();

	public boolean isSuperadmin();

	public User getUser();

	public Collection<? extends GrantedAuthority> getAuthorities();
	
	public boolean hasAuthority(String authority);
	
}
