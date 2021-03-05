package br.com.rmc.cepf.operacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.rmc.BaseException;
import br.com.rmc.BaseService;
import br.com.rmc.Util;
import br.com.rmc.cepf.contabil.ContaContabil;
import br.com.rmc.cepf.contabil.ContaContabilRepository;
import br.com.rmc.cepf.contabil.ContaContabilService;
import br.com.rmc.cepf.contabil.TipoLancamentoContabil;
import br.com.rmc.cepf.contabil.TipoLancamentoContabilRepository;

@Service
class ParcelaService extends BaseService<Parcela> {

	private static final long serialVersionUID = 1980365587115613396L;
	
	@Autowired
	private ContaContabilRepository contaContabilRepository;
	
	@Autowired
	private TipoLancamentoContabilRepository tipoLancamentoContabilRepository;
	
	@Autowired
	private ContaContabilService contaContabilService;
	
	@Autowired
	private OperacaoRepository operacaoRepository;
	
	@Autowired
	private OperacaoService operacaoService;
	
	@Autowired
	private RepasseMensalService repasseMensalService;	

	public ParcelaService(ParcelaRepository repository) {
		super(repository);
	}
	
	@Override
	protected ParcelaRepository getRepository() {
		return (ParcelaRepository) super.getRepository();
	}
	
	@Override
	protected void validateRequireds(Parcela parcela) {
		Assert.notNull(parcela.getNumero(), "Campo obrigatório 'número' não informado");
		Assert.notNull(parcela.getDataDeVencimento(), "Campo obrigatório 'data de vencimento' não informado");
		Assert.notNull(parcela.getValor(), "Campo obrigatório 'valor' não informado");
	}
	
	public List<Parcela> gerarParcelas(Operacao operacao) {
		
		operacao.setParcelas(new ArrayList<Parcela>());
		
	    LocalDate dataDeVencimento = operacao.getData();
	    
	    Long incremento = this.calcularIncrementoDaParcela(operacao.getTipoDePagamento());

	    for (int i = 0; i < operacao.getQuantidadeDeParcelas(); i++) {
	      Parcela parcela = new Parcela(i + 1);
	      
	      dataDeVencimento = incremento == 30 ? dataDeVencimento.plusMonths(1) : dataDeVencimento.plusDays(incremento);
	      
	      parcela.setDataDeVencimento(LocalDate.of(dataDeVencimento.getYear(), dataDeVencimento.getMonth(), dataDeVencimento.getDayOfMonth()));
	      parcela.setValor(this.calcularValorDaParcela(operacao));
	      parcela.setCliente(operacao.getCliente());
	      operacao.getParcelas().add(parcela);
	    }
	    
	    this.calcularReceitaDasParcelas(operacao);

		return operacao.getParcelas();
	}

	private void calcularReceitaDasParcelas(Operacao operacao) {
		
		BigDecimal diferencaCentavos = BigDecimal.ZERO;
		BigDecimal somatorioDeReceitasDasParcelas = BigDecimal.ZERO;
		BigDecimal receitaDaOperacao = BigDecimal.ZERO;		
		
	    //receita da operação é baseada nas parcelas,primeiro cria as parcelas
	    //com os valores e logo após seta a receita de cada parcela
	    BigDecimal receitaDaParcela = operacao.getReceita().divide(new BigDecimal(operacao.getQuantidadeDeParcelas()), 2, RoundingMode.HALF_UP);
	    somatorioDeReceitasDasParcelas = receitaDaParcela.multiply(new BigDecimal(operacao.getQuantidadeDeParcelas()));
	    operacao.getParcelas().forEach(parcela -> parcela.setReceita(receitaDaParcela));
	    
	    if (!operacao.isPagamentoSomenteJuros()) {
			receitaDaOperacao = operacao.getReceita();
			
			if (somatorioDeReceitasDasParcelas.compareTo(receitaDaOperacao) == 1) {
				diferencaCentavos = somatorioDeReceitasDasParcelas.subtract(receitaDaOperacao);
			} else {
				diferencaCentavos = receitaDaOperacao.subtract(somatorioDeReceitasDasParcelas);
			}
			
			Parcela ultimaParcelaParaAjustarCentavosDaReceita = operacao.getParcelas().get(operacao.getParcelas().size() - 1);
			ultimaParcelaParaAjustarCentavosDaReceita.setReceita(ultimaParcelaParaAjustarCentavosDaReceita.getReceita().add(diferencaCentavos));
	    }
	}

	private BigDecimal calcularValorDaParcela(Operacao operacao) {
		BigDecimal valor = operacao.getValor();
		BigDecimal meses = BigDecimal.ZERO;
		BigDecimal taxa = BigDecimal.ZERO;
		BigDecimal porcentagem = BigDecimal.ZERO;
		
		if (operacao.isPagamentoSemanal()) {
			meses = new BigDecimal(operacao.getQuantidadeDeParcelas() / 4);
			taxa = operacao.getTaxa().multiply(meses);
			porcentagem = taxa.divide(new BigDecimal(100));
			valor = valor.add(valor.multiply(porcentagem));
		}
		
		if (operacao.isPagamentoQuinzenal()) {
			meses = new BigDecimal(operacao.getQuantidadeDeParcelas() / 2);
			taxa = operacao.getTaxa().multiply(meses);
			porcentagem = taxa.divide(new BigDecimal(100));
			valor = valor.add(valor.multiply(porcentagem));
		}
		
		if (operacao.isPagamentoMensal()) {
			meses = new BigDecimal(operacao.getQuantidadeDeParcelas());
			taxa = operacao.getTaxa().multiply(meses);
			porcentagem = taxa.divide(new BigDecimal(100));
			valor = valor.add(valor.multiply(porcentagem));
		}
		
		if (operacao.isPagamentoSomenteJuros()) {
			valor = this.calcularValorDaParcelaSomenteJuros(operacao);
		}	
		
		valor = valor.divide(new BigDecimal(operacao.getQuantidadeDeParcelas()), 2, RoundingMode.HALF_UP);
		if (!operacao.isPagamentoSomenteJuros()) {
			valor = Util.round(valor);
		}
		return valor;
	}
	
	private BigDecimal calcularValorDaParcelaSomenteJuros(Operacao operacao) {
		BigDecimal valor = operacao.getValor();
		BigDecimal taxa = BigDecimal.ZERO;
		BigDecimal porcentagem = BigDecimal.ZERO;
		
		taxa = operacao.getTaxa();
		porcentagem = taxa.divide(new BigDecimal(100));
//		valor = valor.multiply(porcentagem, new MathContext(2,  RoundingMode.HALF_UP));
		valor = valor.multiply(porcentagem);

		return valor;
	}	

	private Long calcularIncrementoDaParcela(TipoDePagamento tipoDePagamento) {
		if (TipoDePagamento.SEMANAL.equals(tipoDePagamento)) {
			return 7L;
		} else if (TipoDePagamento.QUINZENAL.equals(tipoDePagamento)) {
			return 15L;
		} else {
			return 30L;
		}
	}	

	public Parcela receberPagamento(Parcela parcela) {
		
		Assert.notNull(parcela, "Parcela não informada.");
		Assert.notNull(parcela.getId(), "Parcela não informada.");
		
		Operacao operacao = this.operacaoRepository.findById(parcela.getCodigoOperacao()).get();
		parcela.setOperacao(operacao);
		
		if (parcela.getValorPago() == null) {
			parcela.setValorPago(parcela.getValor());
		}
		
		//em caso de operacao que não seja somente juros,o valor pago deve ser o valor da parcela
		//operacao com pagamento mensal pode em um mes pagar só o juros
		if ( (!operacao.isPagamentoSomenteJuros() && !operacao.isPagamentoMensal())
				&& parcela.getValorPago().compareTo(parcela.getValor()) != 0) {
			throw new IllegalArgumentException("O valor do pagamento está diferente do valor da parcela.");
		}
		
		//cliente pagou a mais pra abater
		if (operacao.isPagamentoSomenteJuros() && parcela.getValorPago().compareTo(parcela.getValor())  > 0) {
			BigDecimal valorAhAbater = parcela.getValorPago().subtract(parcela.getValor());
			
			operacao.setValor(operacao.getValor().subtract(valorAhAbater));
			String historico = "Atualizando o valor da operação para abater " + 
					Util.formatMoney(valorAhAbater) + ". Valor anterior " + operacao.getValor().add(valorAhAbater);
			operacao.historico(historico);
			this.operacaoService.update(operacao);
			
			RepasseMensal repasseMensal =
					operacao.getRepassesMensais().stream().filter(repasse -> repasse.getDataDoPagamento() == null).findFirst().orElse(null);
			
			if (repasseMensal == null) {
				throw new BaseException("Não existe repasse mensal cadastrado para a parcela em questão.");
			}
			
			repasseMensal.setValor(repasseMensal.getValor().add(valorAhAbater));
			repasseMensal.historico("Valor do repasse atualizado no pagamento da parcela. Cliente abateu " + Util.formatMoney(valorAhAbater));
			this.repasseMensalService.update(repasseMensal);
			
		}
		
		this.realizarLancamentosContabeisReceberPagamento(parcela);
		
		if (operacao.isPagamentoSomenteJuros() && parcela.getGerarProximaParcela()) {
			Parcela novaParcela = this.gerarParcelaSomenteJuros(parcela);
			this.realizarLancamentosContabeisReceberPagamentoSomenteJuros(novaParcela);
		}
		
		parcela = this.update(parcela);
		
		return parcela;
	}
	
	public void receberPagamentoSomenteJuros(Parcela parcela) {
		this.realizarLancamentosContabeisReceberPagamentoSomenteJuros(parcela);
		this.realizarLancamentosContabeisReceberPagamento(parcela);
	}
	
	public void ajustarParcelasAposPagamentoSomenteJuros(Parcela parcelaQuePagouSomenteJuros) {
		
//	    final long incremento = this.calcularIncrementoDaParcela(parcelaQuePagouSomenteJuros.getOperacao().getTipoDePagamento());
	    
	    Operacao operacao = this.operacaoRepository.findById(parcelaQuePagouSomenteJuros.getCodigoOperacao()).get();
		
	    operacao.getParcelas().forEach( parcela -> {
			if (parcela.getDataDoPagamento() == null) {
//				LocalDate dataDeProrrogacao = incremento == 30 ? parcela.getVencimentoAtual().plusMonths(1) : parcela.getVencimentoAtual().plusDays(incremento);
				LocalDate dataDeProrrogacao = parcela.getVencimentoAtual().plusMonths(1);
				parcela.historico(
						"Parcela prorrogada de " + Util.formatLocalDateToString(parcela.getVencimentoAtual()) + 
						" para " + Util.formatLocalDateToString(dataDeProrrogacao) + " devido pagamento de somente o juros da operação.");
				parcela.setDataDeProrrogacao(dataDeProrrogacao);
				this.update(parcela);
			}
		});

	}

	private void realizarLancamentosContabeisReceberPagamento(Parcela parcela) {
		TipoLancamentoContabil tipoPagamentoParcela = new TipoLancamentoContabil("Pagamento de Parcela");
		tipoPagamentoParcela = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoPagamentoParcela)).get();
		
		Operacao operacao = 
				getRepository().findById(
						parcela.getId()).orElseThrow(() -> new RuntimeException("Parcela não encontrada no sistema.")).getOperacao();
		
		String investidor = operacao.getInvestidor().getPessoa().getNome();
		
		ContaContabil contaParcelasAhReceber = new ContaContabil();
		boolean pagouSomenteJuros = operacao.getValorSomenteJuros().compareTo(parcela.getValor()) == 0;
		if (!operacao.isPagamentoSomenteJuros() && !pagouSomenteJuros) {
			contaParcelasAhReceber.setNome(
					ContaContabilService.CONTA_PARCELAS_A_RECEBER + investidor);
		} else {
			contaParcelasAhReceber.setNome(
					ContaContabilService.CONTA_PARCELAS_A_RECEBER_SOMENTE_JUROS + investidor);
		}
		contaParcelasAhReceber = this.contaContabilRepository.findOne(Example.of(contaParcelasAhReceber)).get();
		
		ContaContabil contaReceitaAhReceber = new ContaContabil();
		contaReceitaAhReceber.setNome(ContaContabilService.CONTA_RECEITA_A_RECEBER + investidor);
		contaReceitaAhReceber = this.contaContabilRepository.findOne(Example.of(contaReceitaAhReceber)).get();
		
		ContaContabil contaReceitaRecebida = new ContaContabil();
		contaReceitaRecebida.setNome(ContaContabilService.CONTA_RECEITA_RECEBIDA + investidor);
		contaReceitaRecebida = this.contaContabilRepository.findOne(Example.of(contaReceitaRecebida)).get();
		
		String historico = "Pagamento da parcela de número " + parcela.getNumero() + " da operação " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".";
		
		this.contaContabilService.creditar(contaParcelasAhReceber, parcela.getContaDeDeposito().getContaContabil(),
				parcela.getValorPago(), tipoPagamentoParcela, historico, parcela.getDataDoPagamento(), operacao.getResponsavel());
		
		this.contaContabilService.creditar(contaReceitaRecebida, contaReceitaAhReceber,
				parcela.getReceita(), tipoPagamentoParcela, historico, parcela.getDataDoPagamento(), operacao.getResponsavel());
		
	}
	
	private void realizarLancamentosContabeisReceberPagamentoSomenteJuros(Parcela parcela) {
		
		TipoLancamentoContabil tipoPagamentoParcela = new TipoLancamentoContabil("Pagamento de Parcela Somente Juros");
		tipoPagamentoParcela = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoPagamentoParcela)).get();
		
		Operacao operacao = 
				getRepository().findById(
						parcela.getId()).orElseThrow(() -> new RuntimeException("Parcela não encontrada no sistema.")).getOperacao();
		
		String investidor = operacao.getInvestidor().getPessoa().getNome();
		
		ContaContabil contaParcelasAhReceber = new ContaContabil();
		contaParcelasAhReceber.setNome(
				ContaContabilService.CONTA_PARCELAS_A_RECEBER_SOMENTE_JUROS + investidor);
		contaParcelasAhReceber = this.contaContabilRepository.findOne(Example.of(contaParcelasAhReceber)).get();
		
		ContaContabil contaReceitaAhReceber = new ContaContabil();
		contaReceitaAhReceber.setNome(ContaContabilService.CONTA_RECEITA_A_RECEBER + investidor);
		contaReceitaAhReceber = this.contaContabilRepository.findOne(Example.of(contaReceitaAhReceber)).get();		
				
		String historico = "Pagamento da parcela de número " + parcela.getNumero() + " da operação " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".";
		
		this.contaContabilService.debitar(contaParcelasAhReceber, contaReceitaAhReceber, parcela.getValor(),
				tipoPagamentoParcela, historico, parcela.getDataDoPagamento(), operacao.getResponsavel());
	}
	
	private Parcela gerarParcelaSomenteJuros(Parcela parcela) {
		Parcela parcelaNova = new Parcela();
		parcelaNova.setCliente(parcela.getCliente());
		parcelaNova.setDataDeVencimento(parcela.getVencimentoAtual().plusMonths(1));
		parcelaNova.setNumero(parcela.getNumero()+1);
		parcelaNova.setOperacao(parcela.getOperacao());
		parcelaNova.setValor(this.calcularValorDaParcela(parcela.getOperacao()));
		parcelaNova.setReceita(parcelaNova.getValor());
		return this.persist(parcelaNova);
	}
	
	public Parcela gerarParcelaSomenteJuros(Operacao operacao) {
		Parcela parcelaNova = new Parcela();
		parcelaNova.setCliente(operacao.getCliente());
		parcelaNova.setContaDeDeposito(operacao.getContaCorrentePagamentoSomenteJuros());
		parcelaNova.setDataDeVencimento(operacao.getDataDePagamentoSomenteJuros());
		parcelaNova.setDataDoPagamento(operacao.getDataDePagamentoSomenteJuros());
		parcelaNova.setNumero(operacao.getParcelas().size() + 1);
		parcelaNova.setOperacao(operacao);
		parcelaNova.setValor(this.calcularValorDaParcelaSomenteJuros(operacao));
		parcelaNova.setValorPago(parcelaNova.getValor());
		parcelaNova.setReceita(parcelaNova.getValor());
		String historico = "Parcela criada referente ao pagamento de somente o juros da operação " +
				operacao.getId() + ".";
		parcelaNova.historico(historico);
		return this.persist(parcelaNova);
	}

	public Parcela prorrogar(Parcela parcela, LocalDate dataProrrogacao) {
		Assert.notNull(parcela, "Parcela não informada");
		Assert.notNull(parcela.getId(), "Parcela não informada");
		
		Operacao operacao = this.operacaoRepository.findById(parcela.getCodigoOperacao()).get();
		parcela.setOperacao(operacao);
		
		String historico = "Parcela prorrogada para " + Util.formatLocalDateToString(dataProrrogacao) + ".";
		
		historico += 
				parcela.getDataDeProrrogacao() != null ?
						" Prorrogação anterior era para " + Util.formatLocalDateToString(parcela.getDataDeProrrogacao()) + "." : "";
		
		parcela.historico(historico);
		
		parcela.setDataDeProrrogacao(dataProrrogacao);
		parcela = this.update(parcela);
		
		return parcela;
	}

//	public List<Operacao> find(
//			LocalDate dateDeVencimentoInicial, LocalDate dateDeVencimentoFinal,
//			LocalDate dataDoPagamentoInicial, LocalDate dataDoPagamentoFinal) {
//		
//		Assert.isTrue(dataInicial != null || dataFinal != null, "Pelo menos uma das datas deve ser informada.");
//		
//		return getRepository().findAll(
//				Specification
//				.where(new JoinSpecification<Operacao, Cliente>("cliente", operacao.getCliente()))
//				.and(new JoinSpecification<Operacao, Investidor>("investidor", operacao.getInvestidor()))
//				.and(new JoinSpecification<Operacao, Investidor>("investidorSenior", operacao.getInvestidorSenior()))
//				.and(new JoinSpecification<Operacao, UserApplication>("responsavel", operacao.getResponsavel()))
//				.and(new OrSpecification<Operacao>("tipoDeOperacao", tiposOperacao))
//				.and(new OrSpecification<Operacao>("tipoDePagamento", tiposPagamento))
//				.and(new DateSpecification<Operacao>("data", dataInicial, dataFinal)),
//				Sort.by(Direction.ASC, "data")
//				);
//	}
}
