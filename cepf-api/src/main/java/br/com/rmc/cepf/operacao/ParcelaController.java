package br.com.rmc.cepf.operacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.rmc.BaseController;
import br.com.rmc.BaseException;
import br.com.rmc.Util;
import br.com.rmc.cepf.cliente.Cliente;
import br.com.rmc.cepf.investidor.Investidor;
import br.com.rmc.specification.DateSpecification;
import br.com.rmc.specification.IsNullSpecification;
import br.com.rmc.specification.JoinSpecification;

@RestController
@RequestMapping("/parcelas")
class ParcelaController extends BaseController<Parcela> {
	
	public ParcelaController(ParcelaRepository repository, ParcelaService service) {
		super(repository, service);
	}
	
	@Override
	protected ParcelaRepository getRepository() {
		return (ParcelaRepository) super.getRepository();
	}
	
	@Override
	protected ParcelaService getService() {
		return (ParcelaService) super.getService();
	}	
	
	@Override
	@GetMapping("/deprecated")
	public ResponseEntity<List<Parcela>> find(Optional<String> entityStr, Optional<String> strFrom,
			Optional<String> strTo) {
		return super.find(entityStr, strFrom, strTo);
	}
	
	@GetMapping
	public ResponseEntity<List<Parcela>> find(	
			@RequestParam(value = "dataDeVencimentoInicial", required = false) Optional<String> dataDeVencimentoInicialStr,
			@RequestParam(value = "dataDeVencimentoFinal", required = false) Optional<String> dataDeVencimentoFinalStr,
			@RequestParam(value = "dataDoPagamentoInicial", required = false) Optional<String> dataDoPagamentoInicialStr,
			@RequestParam(value = "dataDoPagamentoFinal", required = false) Optional<String> dataDoPagamentoFinalStr,
			@RequestParam(value = "investidor", required = false) Optional<String> investidorStr,
			@RequestParam(value = "cliente", required = false) Optional<String> clienteStr) {
		
		try {
			List<Parcela> resultList = new ArrayList<Parcela>();
			
			Investidor investidor = investidorStr.isPresent() ? objectMapper.readValue(investidorStr.get(), Investidor.class) : null;
			
			Cliente cliente = clienteStr.isPresent() ? objectMapper.readValue(clienteStr.get(), Cliente.class) : null;
			
			LocalDate dataDeVencimentoInicial = dataDeVencimentoInicialStr.isPresent() ? Util.formatDate(dataDeVencimentoInicialStr.get()) : null;
			LocalDate dataDeVencimentoFinal = dataDeVencimentoFinalStr.isPresent() ? Util.formatDate(dataDeVencimentoFinalStr.get()) : null;
			LocalDate dataDoPagamentoInicial = dataDoPagamentoInicialStr.isPresent() ? Util.formatDate(dataDoPagamentoInicialStr.get()) : null;
			LocalDate dataDoPagamentoFinal = dataDoPagamentoFinalStr.isPresent() ? Util.formatDate(dataDoPagamentoFinalStr.get()) : null;			

			resultList = getRepository().findAll(
					Specification
						.where(new DateSpecification<Parcela>("dataDeVencimento", dataDeVencimentoInicial, dataDeVencimentoFinal))
						.and(new DateSpecification<Parcela>("dataDoPagamento", dataDoPagamentoInicial, dataDoPagamentoFinal))
						.and(new JoinSpecification<Parcela, Cliente>("cliente", cliente)),
						Sort.by(Direction.ASC, "dataDeVencimento"));
			
			resultList.removeIf(parcela -> !parcela.getOperacao().getInvestidor().equals(investidor));

			return ResponseEntity.ok(resultList);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@GetMapping("/em-aberto")
	public ResponseEntity<List<Parcela>> findEmAberto(	
			@RequestParam(value = "dataDeVencimentoInicial", required = false) Optional<String> dataDeVencimentoInicialStr,
			@RequestParam(value = "dataDeVencimentoFinal", required = false) Optional<String> dataDeVencimentoFinalStr,
			@RequestParam(value = "investidor", required = false) Optional<String> investidorStr,
			@RequestParam(value = "cliente", required = false) Optional<String> clienteStr) {
		
		try {
			List<Parcela> resultList = new ArrayList<Parcela>();
			
			Investidor investidor = investidorStr.isPresent() ? objectMapper.readValue(investidorStr.get(), Investidor.class) : null;
					
			Cliente cliente = clienteStr.isPresent() ? objectMapper.readValue(clienteStr.get(), Cliente.class) : null;
			
			LocalDate dataDeVencimentoInicial = dataDeVencimentoInicialStr.isPresent() ? Util.formatDate(dataDeVencimentoInicialStr.get()) : null;
			LocalDate dataDeVencimentoFinal = dataDeVencimentoFinalStr.isPresent() ? Util.formatDate(dataDeVencimentoFinalStr.get()) : null;			

			resultList = getRepository().findAll(
					Specification
						.where(new DateSpecification<Parcela>("dataDeVencimento", dataDeVencimentoInicial, dataDeVencimentoFinal))
						.and(new JoinSpecification<Parcela, Cliente>("cliente", cliente))
						.and(new IsNullSpecification<Parcela>("dataDoPagamento")),
						Sort.by(Direction.ASC, "dataDeVencimento", "dataDeProrrogacao"));

			resultList.removeIf(parcela -> !parcela.getOperacao().getInvestidor().equals(investidor));
			resultList.removeIf(parcela -> parcela.getOperacao().getDataDeAutorizacao() == null);
			resultList.removeIf(parcela -> parcela.getDataDeProrrogacao() != null && parcela.getDataDeProrrogacao().isAfter(dataDeVencimentoFinal));
			
			return ResponseEntity.ok(resultList);
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@PutMapping("/{id}/pagamento")
	public ResponseEntity<Parcela> receberPagamento(@RequestBody Parcela parcela) {
		try {
			return ResponseEntity.ok(getService().receberPagamento(parcela));
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}
	
	@PutMapping("/{id}/prorrogar")
	public ResponseEntity<Parcela> prorrogar(@RequestBody Parcela parcela,
			@RequestParam(value = "dataProrrogacao", required = false) Optional<String> dataProrrogacaoStr) {
		try {
			LocalDate dataProrrogacao = dataProrrogacaoStr.isPresent() ? Util.formatDate(dataProrrogacaoStr.get()) : null;
			return ResponseEntity.ok(getService().prorrogar(parcela, dataProrrogacao));
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}	
}