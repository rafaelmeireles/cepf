package br.com.rmc.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.rmc.security.jwt.JwtTokenProvider;
import br.com.rmc.security.user.User;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<User> signin(@RequestBody User userRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

			String token = jwtTokenProvider.createToken(
					authentication.getName(), 
					authentication.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList()));

			User user = (User) authentication.getPrincipal();
			user.setToken(token);
			return ResponseEntity.ok(user);
		} catch (AuthenticationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Falha no processo de autenticação. Usuário ou senha inválido.", e);
		}
	}    

}