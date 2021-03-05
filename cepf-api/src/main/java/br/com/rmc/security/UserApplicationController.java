package br.com.rmc.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.security.user.UserController;

@RestController
@RequestMapping("/users")
class UserApplicationController extends UserController<UserApplication> {
	
	public UserApplicationController(UserApplicationRepository repository, UserApplicationService service) {
		super(repository, service);
	}
}
