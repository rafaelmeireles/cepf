package br.com.rmc.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import br.com.rmc.BaseController;
import br.com.rmc.BaseException;

public abstract class UserController<T extends User> extends BaseController<T> {
	
	public UserController(JpaRepository<T, Long> repository, UserService<T> service) {
		super(repository, service);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("/reset-password")
	public ResponseEntity resetPassword(@RequestBody T user) {
		try {
			((UserService<T>)service).resetPassword(user);
			return new ResponseEntity(HttpStatus.OK);
		} catch (BaseException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("/change-password")
	public ResponseEntity changePassword(@RequestBody T user) {
		try {
			((UserService<T>)service).changePassword(user);
			return new ResponseEntity(HttpStatus.OK);
		} catch (BaseException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}	
}
