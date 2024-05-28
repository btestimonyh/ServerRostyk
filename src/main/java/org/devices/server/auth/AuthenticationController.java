package org.devices.server.auth;

import lombok.RequiredArgsConstructor;
import org.devices.server.config.JwtService;
import org.devices.server.util.MainLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final JwtService jwtService;
	private final AuthenticationService service;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(
			@RequestBody RegisterRequest request) {
		MainLogger.log.info("Registration requested");
		return ResponseEntity.ok(service.register(request));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticateRequest(
			@RequestBody AuthenticationRequest request) {
		MainLogger.log.info("Authentication requested");
		return ResponseEntity.ok(service.authenticate(request));
	}

	@GetMapping("/type")
	public String getType(@RequestHeader(name = "Authorization") String token) {
		MainLogger.log.info("User Type requested");
		token = token.substring(7);
		return (String) jwtService.extractClaim(token, (c) -> c.get("type"));
	}

	@GetMapping("/ping")
	public Byte ping(@RequestHeader(name = "Authorization") String token) {
		MainLogger.log.info("Ping User Authentication");
		return 1;
	}
}
