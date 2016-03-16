package cz.etnetera.reesmo.model.elasticsearch.result;

import java.util.Arrays;
import java.util.List;

public class TestCategory {

	public static final String FUNCTIONAL = "FUNCTIONAL";
	public static final String PERFORMANCE = "PERFORMANCE";
	public static final String UNIT = "UNIT";
	
	public static List<String> getDeclaredValues() {
		return Arrays.asList(FUNCTIONAL, PERFORMANCE, UNIT);
	}
	
}
