package br.com.rmc.security.role;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.rmc.BaseService;

@Service
class RoleService extends BaseService<Role> {
	
	private static final long serialVersionUID = 4044055203125228964L;
	
	public RoleService(RoleRepository repository) {
		super(repository);
	}
	
	@Override
	protected void validateRequireds(Role role) {
		Assert.notNull(role.getCode(), "Campo obrigatório 'código' não informado");
		Assert.notNull(role.getName(), "Campo obrigatório 'nome' não informado");
		Assert.notNull(role.getActive(), "Campo obrigatório 'ativo' não informado");
	}
}
