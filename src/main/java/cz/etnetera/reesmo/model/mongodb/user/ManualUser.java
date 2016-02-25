package cz.etnetera.reesmo.model.mongodb.user;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import cz.etnetera.reesmo.user.UserRole;
import cz.etnetera.reesmo.user.UserType;

/**
 * Classic user registered over UI.
 */
@Document(collection = "user")
public class ManualUser extends User {

	@Indexed(unique = true, sparse = true)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getRole() {
		return UserRole.ROLE_MANUALUSER;
	}
	
	@Override
	public UserType getType() {
		return UserType.MANUAL;
	}
	
}
