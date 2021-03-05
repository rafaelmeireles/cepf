package br.com.rmc.cepf.contabil;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.BaseController;

@RestController
@RequestMapping("/lancamentos-contabeis/tipos")
class TipoLancamentoContabilController extends BaseController<TipoLancamentoContabil> {
	
	public TipoLancamentoContabilController(TipoLancamentoContabilRepository repository) {
		super(repository);
	}
}
