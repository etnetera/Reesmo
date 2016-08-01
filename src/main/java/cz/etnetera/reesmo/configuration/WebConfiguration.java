package cz.etnetera.reesmo.configuration;

import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.thymeleaf.dialect.DandelionDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Configuration class holding all web related config. Serves as replacement
 * for @EnableWebMvc.
 */
@Configuration
@ComponentScan(basePackages = { "com.github.dandelion.datatables.web" })
public class WebConfiguration extends WebMvcConfigurationSupport {

	/**
	 * 100MB
	 */
	public static final long FILE_SIZE_LIMIT = 104857600l;

	@Value("${thymeleaf.cacheable}")
	private boolean thymeleafCacheable;

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")
				.addResourceLocations("/static/", "/", "classpath:/META-INF/web-resources/").resourceChain(true)
				.addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/");
		registry.addResourceHandler("/robots.txt").addResourceLocations("/");
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(FILE_SIZE_LIMIT);
		return multipartResolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCache(thymeleafCacheable);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.addDialect(new LayoutDialect());
		engine.addDialect(new DandelionDialect());
		engine.addDialect(new DataTablesDialect());
		engine.addDialect(new SpringSecurityDialect());
		return engine;
	}

	@Bean
	public TemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(thymeleafCacheable);
		return resolver;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

}
