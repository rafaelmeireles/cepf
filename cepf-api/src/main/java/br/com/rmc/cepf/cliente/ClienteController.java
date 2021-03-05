package br.com.rmc.cepf.cliente;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.BaseController;

@RestController
@RequestMapping("/clientes")
class ClienteController extends BaseController<Cliente> {
	
	public ClienteController(ClienteRepository repository, ClienteService service) {
		super(repository, service);
	}
}
