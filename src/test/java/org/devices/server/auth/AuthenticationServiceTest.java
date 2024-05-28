package org.devices.server.auth;

import org.devices.server.config.JwtService;
import org.devices.server.user.User;
import org.devices.server.user.UserRepository;
import org.devices.server.user.UserType;
import org.devices.server.user.UserTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {
	@Autowired
	private AuthenticationService authenticationService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private UserTypeRepository userTypeRepository;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	public void contentLoads() {
		assertNotNull(authenticationService);
	}

	@Test
	public void registerTest() {
		RegisterRequest request = new RegisterRequest("1@example.com", "user", "11111111");
		UserType userType = new UserType((byte) 0, "user", null);
		User user = new User();

		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		when(userTypeRepository.findByName(request.getType())).thenReturn(userType);
		when(passwordEncoder.encode(request.getPassword())).thenReturn("1");
		when(userRepository.save(any())).thenReturn(user);
		when(jwtService.generateToken(user)).thenReturn("1");
		assertEquals(new AuthenticationResponse("1"), authenticationService.register(request));
	}

	@Test
	public void registerExceptionTest() {
		RegisterRequest request = new RegisterRequest();

		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
		assertThrows(AuthenticationServiceException.class, () -> authenticationService.register(request));

		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		when(userTypeRepository.findByName(request.getType())).thenReturn(null);
		assertThrows(AuthenticationServiceException.class, () -> authenticationService.register(request));
	}

	@Test
	public void authenticateTest() {
		AuthenticationRequest request = new AuthenticationRequest("1@example.com", "11111111");
		User user = new User();

		when(authenticationManager.authenticate(any())).thenReturn(null);
		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
		when(jwtService.generateToken(user)).thenReturn("1");
		assertEquals(new AuthenticationResponse("1"), authenticationService.authenticate(request));
	}

	@Test
	public void authenticateExceptionTest() {
		AuthenticationRequest request = new AuthenticationRequest("1@example.com", "11111111");
		when(authenticationManager.authenticate(any())).thenReturn(null);
		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));
	}
}