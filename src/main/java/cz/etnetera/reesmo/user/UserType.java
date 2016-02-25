package cz.etnetera.reesmo.user;

public enum UserType {

	MANUAL, API;
	
	public static UserType fromString(String value) {
		for (UserType t : UserType.values()) {
			if (t.name().equalsIgnoreCase(value)) {
				return t;
			}
		}
		return null;
	}
	
	public boolean is(String value) {
		return name().equalsIgnoreCase(value);
	}
	
}
