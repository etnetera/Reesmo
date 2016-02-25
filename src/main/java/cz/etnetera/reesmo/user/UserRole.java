package cz.etnetera.reesmo.user;

public class UserRole {
	
	private static final String ROLE_PREFIX = "ROLE_";
	
	public static final String MANUALUSER = "MANUALUSER";
	public static final String APIUSER = "APIUSER";
	public static final String SUPERADMIN = "SUPERADMIN";
	
	public static final String ROLE_MANUALUSER = ROLE_PREFIX + MANUALUSER;
	public static final String ROLE_APIUSER = ROLE_PREFIX + APIUSER;
	public static final String ROLE_SUPERADMIN = ROLE_PREFIX + SUPERADMIN;
	
}
