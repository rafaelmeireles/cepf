package br.com.rmc.cepf.contabil;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.BaseController;

@RestController
@RequestMapping("/contas-contabeis")
class ContaContabilController extends BaseController<ContaContabil> {
	
	public ContaContabilController(ContaContabilRepository repository) {
		super(repository);
	}
	
	@Override
	protected ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase().withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains());
	}
}
