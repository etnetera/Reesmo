package com.etnetera.projects.testreporting.webapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.mustache.MustacheViewResolver;
import org.springframework.web.servlet.view.mustache.java.MustacheJTemplateFactory;

/**
 * Configuration class holding all web related config. Serves as replacement for @EnableWebMvc.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Override
	public ViewResolver mvcViewResolver() {
		MustacheViewResolver mustacheViewResolver = new MustacheViewResolver();
	    mustacheViewResolver.setPrefix("/WEB-INF/views/");
	    mustacheViewResolver.setSuffix(".html");
	    mustacheViewResolver.setCache(false);
	    mustacheViewResolver.setContentType("text/html;charset=utf-8");
	    
	    MustacheJTemplateFactory mustacheFactory = new MustacheJTemplateFactory();
	    mustacheFactory.setResourceLoader(resourceLoader);
	    
	    mustacheViewResolver.setTemplateFactory(mustacheFactory);
	    return mustacheViewResolver;
	}
	
}
