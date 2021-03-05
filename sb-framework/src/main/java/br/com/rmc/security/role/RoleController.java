package br.com.rmc.security.role;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.BaseController;

@RestController
@RequestMapping("/roles")
class RoleController extends BaseController<Role> {
	
	public RoleController(RoleRepository repository, RoleService service) {
		super(repository, service);
	}
}
