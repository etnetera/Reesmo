package cz.etnetera.reesmo.configuration;

import com.github.dandelion.core.web.DandelionFilter;
import com.github.dandelion.core.web.DandelionServlet;
import cz.etnetera.reesmo.Reesmo;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * Initializes whole webapp and configuration.
 */
public class Initializer extends AbstractSecurityWebApplicationInitializer {

    private static final String CONFIG_LOCATION = Reesmo.PACKAGE + ".configuration";

    private static ApplicationContext applicationContext;
    
    public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void beforeSpringSecurityFilterChain(ServletContext servletContext) {
    	WebApplicationContext context = createContext();
    	applicationContext = context;
        servletContext.addListener(new ContextLoaderListener(context));
        
        // Character Encoding Filter
        FilterRegistration characterEncodingFilter = servletContext.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
        characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
        
        // Resource URL Encoding Filter
        FilterRegistration resourceUrlEncodingFilter = servletContext.addFilter("resourceUrlEncodingFilter",  ResourceUrlEncodingFilter.class);
        resourceUrlEncodingFilter.addMappingForUrlPatterns(null, true, "/*");
        
        // Register the Dandelion filter
        FilterRegistration.Dynamic dandelionFilter = servletContext.addFilter("DandelionFilter", new DandelionFilter());
        dandelionFilter.addMappingForUrlPatterns(null, false, "/*");
        
        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/*");
        
        // Register the Dandelion servlet
        ServletRegistration.Dynamic dandelionServlet = servletContext.addServlet("DandelionServlet", new DandelionServlet());
        dandelionServlet.setLoadOnStartup(2);
        dandelionServlet.addMapping("/dandelion-assets/*");
	}

	private AnnotationConfigWebApplicationContext createContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        return context;
    }

}

