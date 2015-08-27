package com.etnetera.projects.testreporting.webapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	  auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
	}
	
	@Configuration
	public static class ClientSecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().permitAll();
			//http.authorizeRequests().anyRequest().hasRole("USER").and().formLogin();
			/*http
				.authorizeRequests()
				.antMatchers("/login").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().loginPage("/login").loginProcessingUrl("/client_security_check").permitAll()
				.and()
				.sessionManagement().enableSessionUrlRewriting(true)
				.sessionAuthenticationStrategy(new ConcurrentSessionControlAuthenticationStrategy(new SessionRegistryImpl()))
				.sessionCreationPolicy(SessionCreationPolicy.NEVER)
				.and()
				.logout().logoutUrl("/logout").invalidateHttpSession(true).logoutSuccessUrl("/");
			http.headers().cacheControl().disable();
			http.csrf().disable();*/
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**", "/favicon.ico", "/robots.txt");
		}
		
	}
	
	//@Configuration
	//@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.authorizeRequests()
				.antMatchers("/api/**").hasRole("USER")
				.and()
				.httpBasic()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			http.csrf().disable();
		}
		
	}
	
}
