package br.com.rmc.cepf.contabil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.rmc.BaseService;
import br.com.rmc.security.user.User;
import br.com.rmc.specification.DateSpecification;
import br.com.rmc.specification.JoinSpecification;

@Service
public class ContaContabilService extends BaseService<ContaContabil> {
	
	private static final long serialVersionUID = -681678049203666599L;
	
	//Contas de Investidor
	public static final String CONTA_CAPITAL = "Conta Capital - ";
	public static final String CONTA_RECEITA_A_RECEBER = "Conta Receita a Receber - ";
	public static final String CONTA_RECEITA_RECEBIDA = "Conta Receita Recebida - ";
	public static final String CONTA_CORRENTE = "Conta Corrente - ";
	public static final String CONTA_PARCELAS_A_RECEBER = "Conta Parcelas a Receber - ";
	public static final String CONTA_PARCELAS_A_RECEBER_SOMENTE_JUROS = "Conta Parcelas a Receber Somente Juros - ";
	public static final String CONTA_DESPESA = "Conta Despesa - ";
	public static final String CONTA_LUCROS_AH_DISTRIBUIR = "Conta Lucros a Distribuir - ";
	public static final String CONTA_PROVISAO_DE_PERDAS = "Conta Provisão de Perdas - ";
//	public static final String CONTA_DESPESA_BANCARIA = "Conta Despesa Bancaria - ";
//	public static final String CONTA_DESPESA_COM_INVESTIDOR_SENIOR = "Conta Despesa com Investidor Senior - ";

//	Contas de Investidor Senior
//	public static final String CONTA_CAPITAL_INVESTIDOR_SENIOR = "Conta Capital[Investidor Senior] - ";
//	public static final String CONTA_RECEITA_RECEBIDA_INVESTIDOR_SENIOR = "Conta Receita[Investidor Senior] - ";
	
	//Contas de Cliente
	public static final String CONTA_CLIENTE = "Conta Cliente - ";
	
	@Autowired
	private LancamentoContabilService lancamentoContabilService;
	
	@Autowired
	private LancamentoContabilRepository lancamentoContabilRepository;

	public ContaContabilService(ContaContabilRepository repository) {
		super(repository);
	}
	
	public void creditar(ContaContabil contaContabil, ContaContabil contraPartida, BigDecimal valor,
			TipoLancamentoContabil tipoLancamentoContabil, String historico, LocalDate dataDeReferencia, User user) {
		
		Assert.notNull(contaContabil, "Campo obrigatório 'conta contabil' não informado.");
		Assert.notNull(contraPartida, "Campo obrigatório 'contra partida' não informado.");
		Assert.notNull(valor, "Campo obrigatório 'valor' não informado.");
		Assert.notNull(tipoLancamentoContabil, "Campo obrigatório 'tipo lancamento contabil' não informado.");
		Assert.notNull(historico, "Campo obrigatório 'historico' não informado.");
		Assert.notNull(user, "Campo obrigatório 'usuario' não informado.");
		
		LancamentoContabil lancamentoCredito = null;
		LancamentoContabil lancamentoDebito = null;
				
		lancamentoCredito = new LancamentoContabil(contaContabil).credito(valor).tipo(tipoLancamentoContabil)
				.historico(historico).dataReferencia(dataDeReferencia).responsavel(user);
		
		lancamentoDebito = new LancamentoContabil(contraPartida).debito(valor).tipo(tipoLancamentoContabil)
				.historico(historico).dataReferencia(dataDeReferencia).responsavel(user);
		
		lancamentoCredito.setContraPartida(lancamentoDebito);
		
		lancamentoDebito.setContraPartida(lancamentoCredito);
		
		lancamentoContabilService.persist(lancamentoCredito);
		lancamentoContabilService.persist(lancamentoDebito);
		
		contaContabil = this.repository.findById(contaContabil.getId()).get();
		if (contaContabil.isCredora()) {
			contaContabil.creditar(valor);
		} else {
			contaContabil.debitar(valor);
		}
		update(contaContabil);
		
		contraPartida = this.repository.findById(contraPartida.getId()).get();
		if (contraPartida.isCredora()) {
			contraPartida.debitar(valor);
		} else {
			contraPartida.creditar(valor);
		}
		update(contraPartida);
	}
	
	public void debitar(ContaContabil contaContabil, ContaContabil contraPartida,
			BigDecimal valor, TipoLancamentoContabil tipoLancamentoContabil, String historico, LocalDate dataDeReferencia, User user) {
		
		Assert.notNull(contaContabil, "Campo obrigatório 'conta contabil' não informado.");
		Assert.notNull(contraPartida, "Campo obrigatório 'contra partida' não informado.");
		Assert.notNull(valor, "Campo obrigatório 'valor' não informado.");
		Assert.notNull(tipoLancamentoContabil, "Campo obrigatório 'tipo lancamento contabil' não informado.");
		Assert.notNull(historico, "Campo obrigatório 'historico' não informado.");
		Assert.notNull(user, "Campo obrigatório 'usuario' não informado.");		

		LancamentoContabil lancamentoDebito = null;
		LancamentoContabil lancamentoCredito = null;
		
		lancamentoDebito =
				new LancamentoContabil(contaContabil).debito(valor).tipo(tipoLancamentoContabil)
					.historico(historico).dataReferencia(dataDeReferencia).responsavel(user);
		
		lancamentoCredito =
				new LancamentoContabil(contraPartida).credito(valor).tipo(tipoLancamentoContabil)
				.historico(historico).dataReferencia(dataDeReferencia).responsavel(user);
		
		lancamentoDebito.setContraPartida(lancamentoCredito);
		
		lancamentoCredito.setContraPartida(lancamentoDebito);
		
		lancamentoContabilService.persist(lancamentoDebito);
		lancamentoContabilService.persist(lancamentoCredito);
		
		contaContabil = this.repository.findById(contaContabil.getId()).get();
		
		if (contaContabil.isCredora()) {
			contaContabil.debitar(valor);
		} else {
			contaContabil.creditar(valor);
		}
		update(contaContabil);
		
		contraPartida = this.repository.findById(contraPartida.getId()).get();
		if (contraPartida.isCredora()) {
			contraPartida.creditar(valor);
		} else {
			contraPartida.debitar(valor);
		}
		update(contraPartida);		
	}
	
	public BigDecimal getTotalDeEntradas(ContaContabil contaContabil, LocalDate dataInicial, LocalDate dataFinal) {
		List<LancamentoContabil> lancamentos = this.lancamentoContabilRepository.findAll(
				Specification
				.where(new JoinSpecification<LancamentoContabil, ContaContabil>("conta", contaContabil))
				.and(new DateSpecification<LancamentoContabil>("dataDeReferencia", dataInicial, dataFinal))
				);
		
		BigDecimal totalCredito = BigDecimal.ZERO;
		BigDecimal totalDebito = BigDecimal.ZERO;
		
		for (LancamentoContabil lancamentoContabil : lancamentos) {
			totalCredito = totalCredito.add(lancamentoContabil.isCredito() ? lancamentoContabil.getValor() : BigDecimal.ZERO);
			totalDebito = totalDebito.add(lancamentoContabil.isDebito() ? lancamentoContabil.getValor() : BigDecimal.ZERO);			
		}
		
		if (contaContabil.isCredora()) {
			return totalCredito;	
		} else {
			return totalDebito;
		}
	}	
}
