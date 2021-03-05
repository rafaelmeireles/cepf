package br.com.rmc.cepf.conta;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.BaseController;

@RestController
@RequestMapping("/bancos")
class BancoController extends BaseController<Banco> {
	
	public BancoController(BancoRepository repository) {
		super(repository);
	}
}
