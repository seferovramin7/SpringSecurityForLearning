package com.example.telegrambot2023.controller;

import com.example.telegrambot2023.config.JwtTokenUtil;
import com.example.telegrambot2023.dto.SignUpDto;
import com.example.telegrambot2023.entity.UserEntity;
import com.example.telegrambot2023.model.JwtRequest;
import com.example.telegrambot2023.model.JwtResponse;
import com.example.telegrambot2023.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	private final UserRepo userRepo;

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> signIn(@RequestBody JwtRequest request)
			throws Exception {

		authenticate(request.getUsername(), request.getPassword());
		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(request.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	@RequestMapping(value = "/signup",method = RequestMethod.POST)
	public ResponseEntity signUp (@RequestBody SignUpDto dto){
		UserEntity userEntity = UserEntity.builder()
				.email(dto.getEmail())
				.password(dto.getPassword())
				.role(dto.getRole())
				.build();
		userRepo.save(userEntity);
		return ResponseEntity.ok("You signed!");
	}



	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
