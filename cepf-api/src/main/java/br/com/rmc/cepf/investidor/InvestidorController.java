package br.com.rmc.cepf.investidor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.rmc.BaseController;
import br.com.rmc.BaseException;

@RestController
@RequestMapping("/investidores")
class InvestidorController extends BaseController<Investidor> {
	
	public InvestidorController(InvestidorRepository repository, InvestidorService service) {
		super(repository, service);
	}
	
	@Override
	protected InvestidorService getService() {
		return (InvestidorService) super.getService();
	}	
	
	@PostMapping("/aporte")
	public ResponseEntity<?> aporte(@RequestBody Aporte aporte) {
		try {
			getService().aporte(aporte.getInvestidor(), aporte.getValor(), aporte.getContaCorrente(), aporte.getInvestidorSenior());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@PostMapping("/resgate")
	public ResponseEntity<?> resgate(@RequestBody Resgate resgate) {
		try {
			getService().resgate(resgate.getInvestidor(), resgate.getValor(), resgate.getContaCorrente(), resgate.getInvestidorSenior());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@PostMapping("/apuracao-resultado")
	public ResponseEntity<?> apuracaoResultado(@RequestBody ApuracaoResultado apuracaoResultado) {
		try {
			getService().apuracaoResultado(apuracaoResultado.getInvestidor(),
					apuracaoResultado.getPeriodoInicial(), apuracaoResultado.getPeriodoFinal());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}	
}
