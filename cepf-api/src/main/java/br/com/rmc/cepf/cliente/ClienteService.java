package br.com.rmc.cepf.cliente;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.rmc.BaseService;
import br.com.rmc.cepf.contabil.ContaContabil;
import br.com.rmc.cepf.contabil.ContaContabilService;

@Service
class ClienteService extends BaseService<Cliente> {
	
	private static final long serialVersionUID = 4044055203125228964L;
	
	public ClienteService(ClienteRepository repository) {
		super(repository);
	}
	
	@Override
	public Cliente persist(Cliente cliente) {
		cliente.setConta(
				new ContaContabil().nome(ContaContabilService.CONTA_CLIENTE + cliente.getPessoa().getNome()).credora().zeraSaldo()
		);
		
		return super.persist(cliente);
	}
	
	@Override
	protected void validateRequireds(Cliente cliente) {
		Assert.notNull(cliente.getPessoa(), "Campo obrigatório 'pessoa' não informado");
		Assert.notNull(cliente.getPessoa().getNome(), "Campo obrigatório 'nome' não informado");
		Assert.notNull(cliente.getPessoa().getTelefonePrincipal(), "Campo obrigatório 'telefone principal' não informado");
		Assert.notNull(cliente.getInvestidor(), "Campo obrigatório 'investidor' não informado");
		Assert.notNull(cliente.getInvestidor().getId(), "Campo obrigatório 'investidor' não informado");
		
		if (cliente.getReferencia() != null) {
			if ((cliente.getReferencia().getNome() == null || cliente.getReferencia().getNome().isEmpty()) &&
					(cliente.getReferencia().getTelefone() == null || cliente.getReferencia().getTelefone().isEmpty())) {
				cliente.setReferencia(null);
			} else {
				Assert.notNull(cliente.getReferencia().getNome(), "Campo obrigatório 'nome da referência' não informado");
				Assert.isTrue(!cliente.getReferencia().getNome().isEmpty(), "Campo obrigatório 'nome da referência' não informado");
				Assert.notNull(cliente.getReferencia().getTelefone(), "Campo obrigatório 'telefone da referência' não informado");
				Assert.isTrue(!cliente.getReferencia().getTelefone().isEmpty(), "Campo obrigatório 'telefone da referência' não informado");			
			}
		}
	}
}
