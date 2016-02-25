package cz.etnetera.reesmo.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import cz.etnetera.reesmo.http.exception.UnauthorizedException;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectGroupRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;

@Component
public class UserManager {

	private static UserManager instance;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectGroupRepository projectGroupRepository;
	
	private UserManager() {
		instance = this;
	}
	
	public static UserManager getInstance() {
		return instance;
	}

	public List<String> getAllowedProjectIds(Permission permission) {
		return requireAppUser().getAllowedProjectsIds(permission);
	}
	
	public void checkProjectPermission(String projectId, Permission permission) {
		requireAppUser().checkProjectPermission(projectId, permission);
	}
	
	public boolean isAllowedForProject(String projectId, Permission permission) {
		return requireAppUser().isAllowedForProject(projectId, permission);
	}
	
	public String requireUserId() {
		return requireAppUser().getId();
	}
	
	public String getUserId() {
		AppUser appUser = getAppUser();
		if (appUser != null) {
			return appUser.getId();
		}
		return null;
	}
	
	public boolean isSameAsLogged(User user) {
		if (user == null) {
			return false;
		}
		return user.getId().equals(getUserId());
	}
	
	public boolean isSuperadmin() {
		return requireAppUser().isSuperadmin();
	}
	
	public User requireUser() {
		return requireAppUser().getUser();
	}
	
	public User getUser() {
		AppUser appUser = getAppUser();
		if (appUser != null) {
			return appUser.getUser();
		}
		return null;
	}
	
	public void updateUser(User user) {
		requireAppUser().setUser(user);
	}
	
	public AppUser requireAppUser() {
		AppUser appUser = getAppUser();
		if (appUser == null) {
			throw new UnauthorizedException("App user is empty");
		}
		return appUser;
	}
	
	public AppUser getAppUser() {
		Authentication auth = getAuthentication();
		if (auth != null) {
			return (AppUser) auth.getPrincipal();
		}
		return null;
	}
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public void updateUserProjectsPermissions(User user) {
		Map<String, Permission> permissions = new HashMap<>();
		projectRepository.findByUser(user.getId()).forEach(p -> {
			Permission permission = p.getUsers().get(user.getId());
			if (permission != null) {
				permissions.put(p.getId(), permission);
			}
		});
		
		Map<String, Permission> groupPermissions = new HashMap<>();
		projectGroupRepository.findByUser(user.getId()).forEach(pg -> {
			Permission permission = pg.getUsers().get(user.getId());
			if (permission != null) {
				pg.getProjects().forEach(pId -> {
					Permission existingPerm = groupPermissions.get(pId);
					if (existingPerm == null || permission.isGreaterThan(existingPerm)) {
						groupPermissions.put(pId, permission);
					}
				});
			}
		});
		
		// override group permissions with direct project permissions
		groupPermissions.putAll(permissions);
		user.setProjectsPermissions(groupPermissions);
	}
	
}
