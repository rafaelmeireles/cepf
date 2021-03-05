package br.com.rmc.cepf.pessoa;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.BaseController;

@RestController
@RequestMapping("/pessoas")
class PessoaController extends BaseController<Pessoa> {
	
	public PessoaController(PessoaRepository repository, PessoaService service) {
		super(repository, service);
	}
}
