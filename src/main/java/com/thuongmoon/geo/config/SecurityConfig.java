package com.thuongmoon.geo.config;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private JWTRequesFilter jwtrequesFilter;
	private PasswordEncoder passwordEncoder;
	
	  @Autowired
	    private DataSource dataSource;

	    @Autowired
	    private JdbcTemplate jdbcTemplate; 

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				
				.csrf(csrf -> csrf.disable())
//				.sessionManagement(httpSecutityManagementConfiggure -> {
//					httpSecutityManagementConfiggure
//						.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//				})
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/",
								"public",
								"api,/files/**",
								"api/geometry/all",
								"/api/upload",
								"/api/**",
								"/api/posts",
								"/api/auth/profile",
								"/register/**",
								"/api/user/register/**",
								"/user/login",
								"/403", 
								"/css", 
								"/images/**").permitAll()
						
						.requestMatchers("/registration", "/webjars/**").permitAll()
						.requestMatchers(HttpMethod.GET).permitAll()
						.anyRequest().authenticated());

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	 
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails adminDetails = User.builder()
						.username("admin")
						.password("password")
						.roles("ADMIN")
						.build();
		UserDetails user = User.builder()
				.username("User")
				.password("password")
				.roles("USER")
				.build();
		
		return new InMemoryUserDetailsManager(adminDetails,user);
	}
	

}
