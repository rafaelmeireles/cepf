package br.com.rmc.cepf.operacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.rmc.BaseController;
import br.com.rmc.BaseException;

@RestController
@RequestMapping("/operacoes")
class OperacaoController extends BaseController<Operacao> {
	
	public OperacaoController(OperacaoRepository repository, OperacaoService service) {
		super(repository, service);
	}
	
	@Override
	protected OperacaoService getService() {
		return (OperacaoService) super.getService();
	}
	
	@GetMapping("/gerar-parcelas")
	public ResponseEntity<List<Parcela>> gerarParcelas(@RequestParam(value = "entity", required = false) String entityStr) {
		try {
			Operacao operacao = this.objectMapper.readValue(entityStr, getEntityClass());
			List<Parcela> parcelas = getService().gerarParcelas(operacao);
			return ResponseEntity.ok(parcelas);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@Override
	@GetMapping("/deprecated")
	public ResponseEntity<List<Operacao>> find(Optional<String> entityStr, Optional<String> strFrom,
			Optional<String> strTo) {
		return super.find(entityStr, strFrom, strTo);
	}
	
	@GetMapping
	public ResponseEntity<List<Operacao>> find(
			@RequestParam(value = "entity", required = false) Optional<String> entityStr,
			@RequestParam(value = "from", required = false) Optional<LocalDate> dataInicial,
			@RequestParam(value = "to", required = false) Optional<LocalDate> dataFinal,
			@RequestParam(value = "tipoOperacao", required = false) Optional<List<TipoDeOperacao>> tipoOperacao,
			@RequestParam(value = "tipoPagamento", required = false) Optional<List<TipoDePagamento>> tipoPagamento) {
		try {
			List<Operacao> resultList = new ArrayList<Operacao>();

			Operacao operacao = this.objectMapper.readValue(entityStr.get(), Operacao.class);
			resultList = getService().find(
					operacao, dataInicial.orElse(null), dataFinal.orElse(null), 
					tipoOperacao.isPresent() ? tipoOperacao.get() : null,
					tipoPagamento.isPresent() ? tipoPagamento.get() : null);

			return ResponseEntity.ok(resultList);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@PutMapping("/{id}/autorizar")
	public ResponseEntity<Operacao> autorizar(@RequestBody Operacao operacao) {
		try {
			Assert.notNull(operacao, "Operação não informada");
			Assert.notNull(operacao.getId(), "Operação não informada");
			return ResponseEntity.ok(getService().autorizar(operacao));
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@PutMapping("/{id}/finalizar")
	public ResponseEntity<Operacao> finalizar(@RequestBody Operacao operacao) {
		try {
			Assert.notNull(operacao, "Operação não informada");
			Assert.notNull(operacao.getId(), "Operação não informada");
			return ResponseEntity.ok(getService().finalizar(operacao));
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@PutMapping("/{id}/cancelar")
	public ResponseEntity<Operacao> cancelar(@RequestBody Operacao operacao) {
		try {
			Assert.notNull(operacao, "Operação não informada");
			Assert.notNull(operacao.getId(), "Operação não informada");
			return ResponseEntity.ok(getService().cancelar(operacao));
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}	
	
	@PutMapping("/{id}/repasse-mensal/{repasse}/pagar")
	public ResponseEntity<?> pagarRepasseMensal(@RequestBody RepasseMensal repasse) {
		try {
			Assert.notNull(repasse, "Repasse Mensal não informado.");
			Assert.notNull(repasse.getId(), "Repasse Mensal não informado.");
			getService().pagarRepasseMensal(repasse);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@PutMapping("/{id}/pagar-somente-juros")
	public ResponseEntity<Operacao> receberPagamentoSomenteJuros(@RequestBody Operacao operacao) {
		try {
			Assert.notNull(operacao, "Operação não informada");
			Assert.notNull(operacao.getId(), "Operação não informada");
			return ResponseEntity.ok(getService().receberPagamentoSomenteJuros(operacao));
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
}