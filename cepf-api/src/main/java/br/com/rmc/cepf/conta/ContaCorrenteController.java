package br.com.rmc.cepf.conta;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.BaseController;
import br.com.rmc.cepf.conta.ContaCorrente;

@RestController
@RequestMapping("/contas-corrente")
class ContaCorrenteController extends BaseController<ContaCorrente> {
	
	public ContaCorrenteController(ContaCorrenteRepository repository) {
		super(repository);
	}
}
