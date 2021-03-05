package br.com.rmc.cepf.conta;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rmc.BaseController;

@RestController
@RequestMapping("/transferencias")
class TransferenciaController extends BaseController<Transferencia> {
	
	public TransferenciaController(TransferenciaRepository repository) {
		super(repository);
	}
}
