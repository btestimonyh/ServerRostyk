package org.devices.server.auth;

import lombok.RequiredArgsConstructor;
import org.devices.server.config.JwtService;
import org.devices.server.user.User;
import org.devices.server.user.UserRepository;
import org.devices.server.user.UserType;
import org.devices.server.user.UserTypeRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;
	private final UserTypeRepository userTypeRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;

	public AuthenticationResponse register(RegisterRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new AuthenticationServiceException("Email already used");
		}

		UserType userType = userTypeRepository.findByName(request.getType());
		if (userType == null) {
			throw new AuthenticationServiceException("There is no such account type");
		}
		User user = new User();
		user.setEmail(request.getEmail());
		user.setType(userType);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		User saved = userRepository.save(user);

		String jwtToken = jwtService.generateToken(saved);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(), request.getPassword()));

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("Email is not found"));
		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}
}
