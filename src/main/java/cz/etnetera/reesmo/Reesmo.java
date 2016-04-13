package cz.etnetera.reesmo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Reesmo {
	
	public static final String PACKAGE = "cz.etnetera.reesmo";
	
	public static final String ELASTICSEARCH_INDEX = "reesmo";
	
	public static final String NEWLINE = "\n";
	
	@Value("${build.version}")
    private String version;

	public String getVersion() {
		return version;
	}
	
}
