package com.thuongmoon.geo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmoon.geo.config.AuthenticationResponse;
import com.thuongmoon.geo.config.EntityResponse;
import com.thuongmoon.geo.config.JWTTokenUtil;
import com.thuongmoon.geo.models.Role;
import com.thuongmoon.geo.models.User;
import com.thuongmoon.geo.repositories.UserRepository;
import com.thuongmoon.geo.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	@Autowired
	public BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;


	@PostMapping("/register")
	public ResponseEntity<String> reisterUser(@RequestBody User user) {
		try {
			userService.registerUser(user);
			return ResponseEntity.ok("User registered successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
		}

	}
	@PostMapping("/login")
	public ResponseEntity<Object> loginUser(@RequestBody Map<String, String> loginData) {

		try {
			String usernameString = loginData.get("username");
			String paString = loginData.get("password");
			if (!loginData.containsKey("username") || !loginData.containsKey("password")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing username or password");
			}
			User user = userRepository.findByUsername(usernameString);
			if (user == null || !passwordEncoder.matches(paString, user.getPassword())) {
				 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
			}
			final String token = jwtTokenUtil.generateToken(usernameString);
			// final String refresToken = jwtTokenUtil.generateToken(usernameString);
			final Role roleString = user.getRole();
			return EntityResponse.genarateResponse("Authentication", HttpStatus.OK,
					new AuthenticationResponse(token, roleString));
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal server error: " + e.getMessage());
		}
	}
}
