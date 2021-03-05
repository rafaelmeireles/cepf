package br.com.rmc.cepf.contabil;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rmc.BaseController;
import br.com.rmc.BaseException;
import br.com.rmc.Util;

@RestController
@RequestMapping("/lancamentos-contabeis")
//@RequestMapping("/extrato")
class LancamentoContabilController extends BaseController<LancamentoContabil> {
	
	public LancamentoContabilController(LancamentoContabilRepository repository, LancamentoContabilService service) {
		super(repository, service);
	}
	
	@Override
	protected LancamentoContabilService getService() {
		return (LancamentoContabilService)super.getService();
	}
	
	@GetMapping("/extrato")
	public ResponseEntity<List<LancamentoContabil>> find(
			@RequestParam(value = "entity") String entityStr,
			@RequestParam(value = "from", required = false) String strFrom,
			@RequestParam(value = "to", required = false) String strTo) {	
//			@RequestParam(value = "from", required = false) @DateTimeFormat Optional<LocalDate> from,
//			@RequestParam(value = "to", required = false) @DateTimeFormat Optional<LocalDate> to) {
		
		try {
			LancamentoContabil lancamentoContabil = new ObjectMapper().readValue(entityStr, getEntityClass());
			
			LocalDateTime dataInicial = strFrom != null ? Util.formatDateTimeAtStartOfDay(strFrom) : null;
			LocalDateTime dataFinal = strTo != null ? Util.formatDateTimeAtEndOfDay(strTo) : null;
			
			List<LancamentoContabil> lancamentos = 
					getService().find(lancamentoContabil, dataInicial, dataFinal);
			
			return ResponseEntity.ok(lancamentos);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/lancamento-manual")
	public ResponseEntity lancamentoManual(@RequestBody LancamentoContabil lancamentoContabil) {
		try {
			getService().lancamentoManual(lancamentoContabil);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}		
	}
	
//	@GetMapping("extrato")
//	public ResponseEntity<List<LancamentoContabilDTO>> extrato(String entityStr) {
//		ResponseEntity<List<LancamentoContabil>> resultList = super.find(entityStr);
//		List<LancamentoContabilDTO> lancamentos = new ArrayList<LancamentoContabilDTO>();
//		
//		resultList.getBody().forEach(lancamento -> lancamentos.add(
//				new LancamentoContabilDTO().
//				setContraPartida(lancamento.getContraPartida().getId() + " - " + lancamento.getContraPartida().getHistorico()).
//				setCredito(lancamento.getCredito()).
//				setData(lancamento.getData()).
//				setDebito(lancamento.getDebito()).
//				setHistorico(lancamento.getHistorico()).
//				setId(lancamento.getId()).
//				setTipoLancamentoContabil(lancamento.getTipoLancamentoContabil().getId() + " - " + lancamento.getTipoLancamentoContabil().getNome()).
//				setValor(lancamento.getValor())
//		));
//		
//		return ResponseEntity.ok(lancamentos);
//	}
}
