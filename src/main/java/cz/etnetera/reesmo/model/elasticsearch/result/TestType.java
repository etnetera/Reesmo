package cz.etnetera.reesmo.model.elasticsearch.result;

import java.util.Arrays;
import java.util.List;

public class TestType {

	public static final String SELENIUM = "SELENIUM";
	public static final String SMARTMETER = "SMARTMETER";
	public static final String JMETER = "JMETER";
	public static final String GEB = "GEB";
	public static final String SOAPUI = "SOAPUI";
	public static final String TESTNG = "TESTNG";
	public static final String JUNIT = "JUNIT";
	public static final String SPOCK = "SPOCK";
	public static final String SEB = "SEB";
	
	public static List<String> getDeclaredValues() {
		return Arrays.asList(SELENIUM, SMARTMETER, JMETER, GEB, SOAPUI, TESTNG, JUNIT, SPOCK, SEB);
	}
	
}
