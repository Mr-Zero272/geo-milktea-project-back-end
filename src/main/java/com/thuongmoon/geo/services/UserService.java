package com.thuongmoon.geo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thuongmoon.geo.config.JWTTokenUtil;
import com.thuongmoon.geo.models.Role;
import com.thuongmoon.geo.models.User;
import com.thuongmoon.geo.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	public BCryptPasswordEncoder passwordEncoder;

	public void registerUser(User user) throws Exception {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new Exception("User Alredy exist");
		}
		user.setRole(Role.USER);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	public String loginUser(String username, String password) throws Exception {
		User userLogin = userRepository.findByUsername(username);
		if (userLogin == null) {
			throw new UsernameNotFoundException("User not found");
		}
		if (!passwordEncoder.matches(password, userLogin.getPassword())) {
			throw new BadCredentialsException("Invailid password");
		}

		return "lOGIN SUCCESS";
	}

	public String Authenticate(String token) {

		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String username = jwtTokenUtil.getUsernameFromToken(token);
		User user = findByUsername(username);
		if (user != null) {
			return username;
		} else {
			return null;
		}

	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
