package cz.etnetera.reesmo.model.mongodb.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import cz.etnetera.reesmo.user.UserRole;
import cz.etnetera.reesmo.user.UserType;

@Document(collection = "user")
public class ApiUser extends User {
	
	private List<String> allowedIps = new ArrayList<>();

	public List<String> getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}

	@Override
	public String getRole() {
		return UserRole.ROLE_APIUSER;
	}

	@Override
	public UserType getType() {
		return UserType.API;
	}
	
}
