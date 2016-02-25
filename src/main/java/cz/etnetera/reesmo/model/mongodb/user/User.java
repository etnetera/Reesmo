package cz.etnetera.reesmo.model.mongodb.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import cz.etnetera.reesmo.http.exception.ForbiddenException;
import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import cz.etnetera.reesmo.user.IdentifiedUser;
import cz.etnetera.reesmo.user.UserRole;
import cz.etnetera.reesmo.user.UserType;

abstract public class User extends MongoAuditedModel implements IdentifiedUser {

	@Id
	private String id;
	
	/**
	 * Is used for filtering mainly.
	 * No need to be unique.
	 */
	private String label;
	
	@Indexed(unique = true)
	private String username;
	
	private String password;
	
	/**
	 * User can be deactivated but still kept in app.
	 */
	private boolean enabled;
	
	/**
	 * If true than this user is allowed for everything
	 * and is not affected with permissions.
	 */
	private boolean superadmin;
	
	/**
	 * Compound project and project group permissions.
	 * Key is project id.
	 */
	private Map<String, Permission> projectsPermissions = new HashMap<>();
	
	@Transient
	private Collection<? extends GrantedAuthority> authorities;
	
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isSuperadmin() {
		return superadmin;
	}

	public void setSuperadmin(boolean superadmin) {
		this.superadmin = superadmin;
	}
	
	public Map<String, Permission> getProjectsPermissions() {
		return projectsPermissions;
	}

	public void setProjectsPermissions(Map<String, Permission> projectsPermissions) {
		this.projectsPermissions = projectsPermissions;
	}

	public List<String> getAllowedProjectsIds(Permission permission) {
		if (superadmin) {
			return null;
		}
		List<String> projectsIds = new ArrayList<>();
		if (permission != null) {
			for (Map.Entry<String, Permission> entry : getProjectsPermissions().entrySet()) {
				if (entry.getValue().isGreaterThanOrEqual(permission)) {
					projectsIds.add(entry.getKey());
				}
			}
		}
		return projectsIds;
	}
	
	public void checkProjectPermission(String projectId, Permission permission) {
		if (!isAllowedForProject(projectId, permission)) {
			throw new ForbiddenException("User with id " + id + " has not " + permission + " permission for project with id " + projectId + ".");
		}
	}
	
	public boolean isAllowedForProject(String projectId, Permission permission) {
		if (superadmin) {
			return true;
		}
		if (projectId != null && permission != null) {
			for (Map.Entry<String, Permission> entry : getProjectsPermissions().entrySet()) {
				if (entry.getKey().equals(projectId)) {
					return entry.getValue().isGreaterThanOrEqual(permission);
				}
			}
		}
		return false;
	}
	
	public void checkUserPermission(IdentifiedUser user, Permission permission) {
		if (!isAllowedForUser(user, permission)) {
			throw new ForbiddenException("User with id " + user == null ? null
					: user.getId() + " has not " + permission + " permission for user with id " + getId() + ".");
		}
	}

	public boolean isAllowedForUser(IdentifiedUser user, Permission permission) {
		if (user == null) {
			return false;
		}
		if (user.isSuperadmin()) {
			return true;
		}
		if (permission == null) {
			return false;
		}
		if (permission.isGreaterThan(Permission.EDITOR)) {
			return false;
		}
		return user.getId().equals(getId());
	}
	
	public void checkUserPermission(IdentifiedUser user, String permission) {
		checkUserPermission(user, Permission.fromString(permission));
	}
	
	public boolean isAllowedForUser(IdentifiedUser user, String permission) {
		return isAllowedForUser(user, Permission.fromString(permission));
	}
	
	@Override
	public User getUser() {
		return this;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (authorities == null) {
			List<SimpleGrantedAuthority> auths = new ArrayList<>();
			auths.add(new SimpleGrantedAuthority(getRole()));
			if (superadmin) {
				auths.add(new SimpleGrantedAuthority(UserRole.ROLE_SUPERADMIN));
			}
			authorities = auths;
		}
		return authorities;
	}

	@Override
	public boolean hasAuthority(String authority) {
		return authority != null && getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authority));
	}
	
	public abstract String getRole();
	
	public abstract UserType getType();
	
}
