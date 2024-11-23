package io.security.springsecuritymaster;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
			.formLogin(form -> form
				.loginPage("/loginPage")
				.loginProcessingUrl("/loginProc")
				.defaultSuccessUrl("/", true)
				.failureUrl("/failed")
				.usernameParameter("userId")
				.passwordParameter("passwd")
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
						Authentication authentication) throws IOException, ServletException {
						log.info("authentication: " + authentication);
						response.sendRedirect("/home");
					}
				})
				.failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
						AuthenticationException exception) throws IOException, ServletException {
						log.info("exception: " + exception.getMessage());
						response.sendRedirect("/login");
					}
				})
				.permitAll()
			)
			.build();
	}


	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withUsername("user")
			.password("{noop}1111")
			.roles("USER")
			.build();
		return new InMemoryUserDetailsManager(user);
	}
}
