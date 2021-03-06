package br.com.rmc.cepf.operacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.rmc.BaseService;
import br.com.rmc.cepf.cliente.Cliente;
import br.com.rmc.cepf.contabil.ContaContabil;
import br.com.rmc.cepf.contabil.ContaContabilRepository;
import br.com.rmc.cepf.contabil.ContaContabilService;
import br.com.rmc.cepf.contabil.TipoLancamentoContabil;
import br.com.rmc.cepf.contabil.TipoLancamentoContabilRepository;
import br.com.rmc.cepf.investidor.Investidor;
import br.com.rmc.cepf.investidor.InvestidorService;
import br.com.rmc.security.UserApplication;
import br.com.rmc.specification.DateSpecification;
import br.com.rmc.specification.JoinSpecification;
import br.com.rmc.specification.OrSpecification;

@Service
class OperacaoService extends BaseService<Operacao> {

	private static final long serialVersionUID = 1980365587115613396L;
	
	@Autowired
	private ContaContabilRepository contaContabilRepository;
	
	@Autowired
	private TipoLancamentoContabilRepository tipoLancamentoContabilRepository;
	
	@Autowired
	private ContaContabilService contaContabilService;
	
	@Autowired
	private InvestidorService investidorService;
	
	@Autowired
	private RepasseMensalService repasseMensalService;
	
	@Autowired
	private ParcelaService parcelaService;

	public OperacaoService(OperacaoRepository repository) {
		super(repository);
	}
	
	@Override
	protected OperacaoRepository getRepository() {
		return (OperacaoRepository) super.getRepository();
	}
	
	@Override
	public Operacao persist(Operacao operacao) {
		
		operacao.getParcelas().forEach(parcela -> parcela.setOperacao(operacao));
		this.gerarNumeroDaOperacao(operacao);
		
		if (operacao.isRepasse()) {
			this.repasseMensalService.gerarRepasseMensal(operacao);
		}
		
		return super.persist(operacao);
	}

	private void gerarNumeroDaOperacao(Operacao operacao) {
		List<Operacao> operacoesDoCliente = getRepository().findAll(
				Specification
					.where(new JoinSpecification<Operacao, Cliente>("cliente", operacao.getCliente())),
					Sort.by(Direction.ASC, "numero"));
		
		int numero = 1;
		if (!operacoesDoCliente.isEmpty()) {	
			numero = operacoesDoCliente.get(operacoesDoCliente.size() - 1).getNumero() + 1;
		}
		
		operacao.setNumero(numero);
	}

	@Override
	protected void validateRequireds(Operacao operacao) {
		Assert.notNull(operacao.getCliente(), "Campo obrigat??rio 'cliente' n??o informado");
		Assert.notNull(operacao.getData(), "Campo obrigat??rio 'data' n??o informado");
		Assert.notNull(operacao.getValor(), "Campo obrigat??rio 'valor' n??o informado");
		Assert.notNull(operacao.getTaxa(), "Campo obrigat??rio 'taxa' n??o informado");
		Assert.notNull(operacao.getQuantidadeDeParcelas(), "Campo obrigat??rio 'quantidade de parcelas' n??o informado");
		Assert.notNull(operacao.getInvestidor(), "Campo obrigat??rio 'investidor' n??o informado");
		Assert.notNull(operacao.getTipoDeOperacao(), "Campo obrigat??rio 'tipo de opera????o' n??o informado");
		Assert.notNull(operacao.getTipoDePagamento(), "Campo obrigat??rio 'tipo de pagamento' n??o informado");
		Assert.notNull(operacao.getResponsavel(), "Campo obrigat??rio 'respons??vel' n??o informado");
	}
	
	public List<Operacao> find(Operacao operacao, LocalDate dataInicial, LocalDate dataFinal,
			List<TipoDeOperacao> tiposOperacao, List<TipoDePagamento> tiposPagamento) {
		
		return getRepository().findAll(
				Specification
				.where(new JoinSpecification<Operacao, Cliente>("cliente", operacao.getCliente()))
				.and(new JoinSpecification<Operacao, Investidor>("investidor", operacao.getInvestidor()))
				.and(new JoinSpecification<Operacao, Investidor>("investidorSenior", operacao.getInvestidorSenior()))
				.and(new JoinSpecification<Operacao, UserApplication>("responsavel", operacao.getResponsavel()))
				.and(new OrSpecification<Operacao>("tipoDeOperacao", tiposOperacao))
				.and(new OrSpecification<Operacao>("tipoDePagamento", tiposPagamento))
				.and(new DateSpecification<Operacao>("data", dataInicial, dataFinal)),
				Sort.by(Direction.ASC, "data")
				);
	}	

	public List<Parcela> gerarParcelas(Operacao operacao) {
		this.validateRequireds(operacao);
		this.parcelaService.gerarParcelas(operacao);
		return operacao.getParcelas();
	}
	
	public Operacao autorizar(Operacao operacao) {
		Assert.isNull(operacao.getDataDeAutorizacao(), "Est?? opera????o j?? foi autorizada.");
		
		operacao.setDataDeAutorizacao(LocalDateTime.now());
		operacao.getTransferencia().setData(LocalDateTime.now());
		operacao.getTransferencia().setValor(operacao.getValor());
		
		this.realizarLancamentosContabeisAutorizar(operacao);
		
		operacao = this.update(operacao);
		
		return operacao;
	}
	
	private void realizarLancamentosContabeisAutorizar(Operacao operacao) {
		TipoLancamentoContabil tipoRecebimentoDeOperacao = new TipoLancamentoContabil("Recebimento de Opera????o");
		tipoRecebimentoDeOperacao = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoRecebimentoDeOperacao)).get();
		
		TipoLancamentoContabil tipoFatorDesagio = new TipoLancamentoContabil("Fator Desagio");
		tipoFatorDesagio = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoFatorDesagio)).get();
		
		TipoLancamentoContabil tipoTransferencia = new TipoLancamentoContabil("Transferencia entre contas");
		tipoTransferencia = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoTransferencia)).get();
		
		TipoLancamentoContabil tipoTarifaTransferencia = new TipoLancamentoContabil("Tarifa Bancaria - DOC/TED");
		tipoTarifaTransferencia = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoTarifaTransferencia)).get();		
		
		ContaContabil contaCliente = new ContaContabil();
		contaCliente.setNome(ContaContabilService.CONTA_CLIENTE + operacao.getCliente().getPessoa().getNome());
		contaCliente = this.contaContabilRepository.findOne(Example.of(contaCliente)).get();
		
		ContaContabil contaParcelasAhReceber = new ContaContabil();
		if (!operacao.isPagamentoSomenteJuros()) {
			contaParcelasAhReceber.setNome(ContaContabilService.CONTA_PARCELAS_A_RECEBER + operacao.getInvestidor().getPessoa().getNome());
		} else {
			contaParcelasAhReceber.setNome(ContaContabilService.CONTA_PARCELAS_A_RECEBER_SOMENTE_JUROS + operacao.getInvestidor().getPessoa().getNome());
		}
		contaParcelasAhReceber = this.contaContabilRepository.findOne(Example.of(contaParcelasAhReceber)).get();
		
		ContaContabil contaReceitaAhReceber = new ContaContabil();
		contaReceitaAhReceber.setNome(ContaContabilService.CONTA_RECEITA_A_RECEBER + operacao.getInvestidor().getPessoa().getNome());
		contaReceitaAhReceber = this.contaContabilRepository.findOne(Example.of(contaReceitaAhReceber)).get();
		
		ContaContabil contaReceitaRecebida = new ContaContabil();
		contaReceitaRecebida.setNome(ContaContabilService.CONTA_RECEITA_RECEBIDA + operacao.getInvestidor().getPessoa().getNome());
		contaReceitaRecebida = this.contaContabilRepository.findOne(Example.of(contaReceitaRecebida)).get();
		
		this.contaContabilService.creditar(contaCliente, contaParcelasAhReceber, operacao.getValor().add(operacao.getReceita()), tipoRecebimentoDeOperacao,
				"Recebimento da opera????o de n??mero " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".", operacao.getDataDeAutorizacao().toLocalDate(), operacao.getResponsavel());
		
		this.contaContabilService.creditar(contaReceitaAhReceber, contaCliente, operacao.getReceita(), tipoFatorDesagio,
				"Fator Desagio refer??nte a opera????o de n??mero " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".", operacao.getDataDeAutorizacao().toLocalDate(), operacao.getResponsavel());
		
		if (operacao.getTransferencia().getTarifa() != null && operacao.getTransferencia().getTarifa().compareTo(BigDecimal.ZERO) > 0) {
			this.contaContabilService.debitar(contaCliente, contaReceitaRecebida, operacao.getTransferencia().getTarifa(), tipoTarifaTransferencia,
					"Tarifa de transferencia referente a opera????o de n??mero " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".", operacao.getDataDeAutorizacao().toLocalDate(), operacao.getResponsavel());
		}
		
		BigDecimal tarifa = operacao.getTransferencia().getTarifa() != null ? operacao.getTransferencia().getTarifa() : BigDecimal.ZERO;
		
		this.contaContabilService.debitar(
				contaCliente, operacao.getTransferencia().getContaOrigem().getContaContabil(),
				operacao.getValor().subtract(tarifa), tipoTransferencia,
				"Transferencia de valor referente a opera????o de n??mero " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".", operacao.getDataDeAutorizacao().toLocalDate(), operacao.getResponsavel());
	}

	public Operacao finalizar(Operacao operacao) {
		Assert.notNull(operacao, "Opera????o n??o informada");
		Assert.notNull(operacao.getId(), "Opera????o n??o informada");		
		Assert.isNull(operacao.getDataFinalizacao(), "Est?? opera????o j?? foi finalizada.");
		
		operacao.setDataFinalizacao(LocalDateTime.now());
		
		if (operacao.isPagamentoSomenteJuros()) {
			this.realizarLancamentosContabeisAoFinalizarOperacaoSomenteJuros(operacao);
			
			if (operacao.isRepasse()) {
				String historico = "Resgate - " + operacao.getInvestidorSenior().getPessoa().getNome() + ", referente ao pagamento da opera????o " + operacao.getId() + " do tipo somente juros.";
				this.investidorService.resgate(
						operacao.getInvestidor(), operacao.getValor(), operacao.getContaCorrentePagamentoDoRepasseSomenteJuros(),
						operacao.getInvestidorSenior(), historico, LocalDate.now());
			}
		}
		
		operacao = this.update(operacao);
		
		return operacao;
	}

	private void realizarLancamentosContabeisAoFinalizarOperacaoSomenteJuros(Operacao operacao) {
		TipoLancamentoContabil tipoFinalizandoOpera????oSomenteJuros = new TipoLancamentoContabil("Finalizando Opera????o Somente Juros");
		tipoFinalizandoOpera????oSomenteJuros = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoFinalizandoOpera????oSomenteJuros)).get();
		
		ContaContabil contaParcelasAhReceber = new ContaContabil();
		contaParcelasAhReceber.setNome(ContaContabilService.CONTA_PARCELAS_A_RECEBER_SOMENTE_JUROS + operacao.getInvestidor().getPessoa().getNome());
		contaParcelasAhReceber = this.contaContabilRepository.findOne(Example.of(contaParcelasAhReceber)).get();
		
		this.contaContabilService.creditar(
				contaParcelasAhReceber, operacao.getContaCorrentePagamentoSomenteJuros().getContaContabil(),
				operacao.getValor(), tipoFinalizandoOpera????oSomenteJuros,
				"Pagamento de Investidor Senior referente a opera????o " + operacao.getId(), operacao.getDataFinalizacao().toLocalDate(),
				operacao.getResponsavel());
	}
	
	public void pagarRepasseMensal(RepasseMensal repasse) {
		this.repasseMensalService.pagarRepasseMensal(repasse);
	}
	
	public Operacao receberPagamentoSomenteJuros(Operacao operacao) {
		Assert.notNull(operacao, "Operacao n??o informada.");
		Assert.notNull(operacao.getId(), "Operacao n??o informada.");
		
		String message = "Opera????o com pagamento completo n??o pode receber pagamento somente juros. ";
		if (operacao.isRepasse()) {
			message += "Favor pagar repasse e finaliza-l??";
		} else {
			message += "Favor finaliza-l??";	
		}
		
		Assert.isTrue(
				!operacao.getParcelas().stream().allMatch(parcela -> parcela.getDataDoPagamento() != null),
					message);
		
		Parcela parcelaNova = this.parcelaService.gerarParcelaSomenteJuros(operacao);
		this.parcelaService.receberPagamentoSomenteJuros(parcelaNova);
		this.parcelaService.ajustarParcelasAposPagamentoSomenteJuros(parcelaNova);
		
		if (operacao.isRepasse()) {
			RepasseMensal repasseNovo = this.repasseMensalService.gerarRepasseMensalParaPagamentoSomenteJuros(parcelaNova);
			this.repasseMensalService.ajustarRepassesAposPagamentoSomenteJuros(repasseNovo);
		}
		
		return operacao;
	}

	public Operacao cancelar(Operacao operacao) {
		
		Assert.notNull(operacao, "Operacao n??o informada.");
		Assert.notNull(operacao.getId(), "Operacao n??o informada.");
		
		operacao.setDataDeCancelamento(LocalDate.now());
		operacao.getParcelas().forEach(parcela -> parcela.setDataDeCancelamento(LocalDate.now()));
		
		if (operacao.isRepasse()) {
			operacao.getRepassesMensais().forEach(repasse -> repasse.setDataDeCancelamento(LocalDate.now()));	
		}
		
		operacao = this.update(operacao);
		
//		if (operacao.getDataDeAutorizacao() != null) {
//			this.realizarLancamentosContabeisCancelarOperacao(operacao);
//		}
		
		
		return operacao;
	}

	@Deprecated
	@SuppressWarnings("unused")
	private void realizarLancamentosContabeisCancelarOperacao(Operacao operacao) {
		TipoLancamentoContabil tipoLancamento = new TipoLancamentoContabil("Cancelamento de Opera????o");
		tipoLancamento = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoLancamento)).get();
		
		ContaContabil contaReceitaAhReceber = new ContaContabil();
		contaReceitaAhReceber.setNome(ContaContabilService.CONTA_RECEITA_A_RECEBER + operacao.getInvestidor().getPessoa().getNome());
		contaReceitaAhReceber = this.contaContabilRepository.findOne(Example.of(contaReceitaAhReceber)).get();
		
		ContaContabil contaParcelasAhReceber = new ContaContabil();
		if (!operacao.isPagamentoSomenteJuros()) {
			contaParcelasAhReceber.setNome(ContaContabilService.CONTA_PARCELAS_A_RECEBER + operacao.getInvestidor().getPessoa().getNome());
		} else {
			contaParcelasAhReceber.setNome(ContaContabilService.CONTA_PARCELAS_A_RECEBER_SOMENTE_JUROS + operacao.getInvestidor().getPessoa().getNome());
		}
		contaParcelasAhReceber = this.contaContabilRepository.findOne(Example.of(contaParcelasAhReceber)).get();
		
		ContaContabil contaCliente = new ContaContabil();
		contaCliente.setNome(ContaContabilService.CONTA_CLIENTE + operacao.getCliente().getPessoa().getNome());
		contaCliente = this.contaContabilRepository.findOne(Example.of(contaCliente)).get();
		
		this.contaContabilService.debitar(contaReceitaAhReceber, contaParcelasAhReceber, operacao.getReceita(), tipoLancamento,
				"Cancelamento da opera????o de n??mero " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".", LocalDate.now(), operacao.getResponsavel());
		
		this.contaContabilService.debitar(contaCliente, contaParcelasAhReceber, operacao.getValor(), tipoLancamento,
				"Cancelamento da opera????o de n??mero " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".", LocalDate.now(), operacao.getResponsavel());
		
	}
}
