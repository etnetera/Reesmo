package cz.etnetera.reesmo.model.datatables.user;

import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.AuditedModelDT;
import cz.etnetera.reesmo.model.mongodb.user.User;

import java.util.Locale;

public class UserDT extends AuditedModelDT {

	private String id;
	
	private String label;
	
	private String username;
	
	private String type;
	
	private String enabled;
	
	private String superadmin;
	
	public UserDT(User user, Localizer localizer, Locale locale) {
		super(user);
		this.id = user.getId();
		this.label = user.getLabel();
		this.username = user.getUsername();
		this.type = localizer.localize(user.getType(), locale);
		this.enabled = localizer.localize(user.isEnabled(), locale);
		this.superadmin = localizer.localize(user.isSuperadmin(), locale);
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getUsername() {
		return username;
	}

	public String getType() {
		return type;
	}

	public String getEnabled() {
		return enabled;
	}

	public String getSuperadmin() {
		return superadmin;
	}
	
}
