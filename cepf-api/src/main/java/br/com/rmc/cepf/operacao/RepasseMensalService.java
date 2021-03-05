package br.com.rmc.cepf.operacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import br.com.rmc.cepf.investidor.InvestidorService;

@Service
class RepasseMensalService extends BaseService<RepasseMensal> {
	
	private static final long serialVersionUID = 5814925969377570390L;
	
	@Autowired
	private TipoLancamentoContabilRepository tipoLancamentoContabilRepository;
	
	@Autowired
	private ContaContabilRepository contaContabilRepository;
	
	@Autowired
	private ContaContabilService contaContabilService;	
	
	@Autowired
	private OperacaoRepository operacaoRepository;
	
	@Autowired
	private InvestidorService investidorService;	
	
	public RepasseMensalService(RepasseMensalRepository repository) {
		super(repository);
	}
	
	public void gerarRepasseMensal(Operacao operacao) {
		
		Integer quantidadeDeRepasses = 1; 
		
		if (!operacao.isPagamentoSomenteJuros()) {
			quantidadeDeRepasses = operacao.isPagamentoSemanal() ? operacao.getQuantidadeDeParcelas() / 4 : 
				operacao.isPagamentoQuinzenal() ? operacao.getQuantidadeDeParcelas() / 2 : operacao.getQuantidadeDeParcelas();
		}
			
		LocalDate vencimento = operacao.getData();
		operacao.setRepassesMensais(new ArrayList<RepasseMensal>());
		BigDecimal diferencaCentavos = BigDecimal.ZERO;
		BigDecimal somatorioDeRepasses = BigDecimal.ZERO;
		BigDecimal agioEfetivo = BigDecimal.ZERO;
		
		for (int i = 0; i < quantidadeDeRepasses; i++) {
			vencimento = vencimento.plusMonths(1);
			
			BigDecimal agioDeRepasse = operacao.getReceitaDeRepasse(quantidadeDeRepasses)
					.divide(new BigDecimal(quantidadeDeRepasses), 2, RoundingMode.HALF_UP);
			
			RepasseMensal repasseMensal = new RepasseMensal();
			repasseMensal.setNumero(i+1);
			repasseMensal.setDataDeVencimento(vencimento);
			repasseMensal.setValor(this.calcularValorDoRepasse(operacao));
			repasseMensal.setOperacao(operacao);
			repasseMensal.setAgio(agioDeRepasse);
			
			operacao.getRepassesMensais().add(repasseMensal);
			
			somatorioDeRepasses = somatorioDeRepasses.add(repasseMensal.getValor());
		}
		
		if (!operacao.isPagamentoSomenteJuros()) {
			agioEfetivo = operacao.getValor().add(operacao.getReceitaDeRepasse());
			
			if (somatorioDeRepasses.compareTo(agioEfetivo) == 1) {
				diferencaCentavos = somatorioDeRepasses.subtract(agioEfetivo);
			} else {
				diferencaCentavos = agioEfetivo.subtract(somatorioDeRepasses);
			}
			
			RepasseMensal ultimoRepasseParaAjustarCentavos = operacao.getRepassesMensais().get(operacao.getRepassesMensais().size() - 1);
			ultimoRepasseParaAjustarCentavos.setValor(ultimoRepasseParaAjustarCentavos.getValor().subtract(diferencaCentavos));
		}
	}
	
	private BigDecimal calcularValorDoRepasse(Operacao operacao) {
		Integer quantidadeDeRepasses = 1; 
		
		if (!operacao.isPagamentoSomenteJuros()) {
			quantidadeDeRepasses = operacao.isPagamentoSemanal() ? operacao.getQuantidadeDeParcelas() / 4 : 
				operacao.isPagamentoQuinzenal() ? operacao.getQuantidadeDeParcelas() / 2 : operacao.getQuantidadeDeParcelas();
		}
		
		BigDecimal valor = operacao.getValor().divide(new BigDecimal(quantidadeDeRepasses), 2, RoundingMode.HALF_UP);
		BigDecimal porcentagem = operacao.getTaxaDeRepasse().divide(new BigDecimal(100));
		
		if (operacao.isPagamentoSomenteJuros()) {
			valor = operacao.getValor().multiply(porcentagem).setScale(2, RoundingMode.HALF_UP);
		} else {
			valor = valor.add(operacao.getValor().multiply(porcentagem));
		}
		
		return valor;
	}
	
	private BigDecimal calcularValorDoRepasseSomenteJuros(Operacao operacao) {
		BigDecimal porcentagem = operacao.getTaxaDeRepasse().divide(new BigDecimal(100));
		return operacao.getValor().multiply(porcentagem).setScale(2, RoundingMode.HALF_UP);
	}	
	
	public void pagarRepasseMensal(RepasseMensal repasse) {
		
		Assert.notNull(repasse, "Repasse Mensal não informado.");
		Assert.notNull(repasse.getId(), "Repasse Mensal não informado.");
		Assert.notNull(repasse.getContaCorrente(), "Conta corrente para o repasse não informada.");
		Assert.notNull(repasse.getContaCorrente().getId(), "Conta corrente para o repasse não informada.");
		Assert.isNull(repasse.getDataDoPagamento(), "O repasse informado já foi pago.");
		
		Operacao operacao = this.operacaoRepository.findById(repasse.getCodigoOperacao()).
				orElseThrow(() -> new BaseException("Operação do repasse não encontrada."));
		
		Assert.isTrue(operacao.isRepasse(), "Pagamento de repasse mensal só é permitido para operação do tipo repasse.");
		
		List<Parcela> parcelasDoRepasse = operacao.getParcelas().stream().filter(
				parcela -> parcela.getVencimentoAtual().equals(repasse.getVencimentoAtual()) ||
				parcela.getVencimentoAtual().isBefore(repasse.getVencimentoAtual())).collect(Collectors.toList());
		
		parcelasDoRepasse.forEach( parcela -> {
			if (parcela.getDataDoPagamento() == null) {
				throw new BaseException("Repasse não pôde ser efetuado. Existem parcelas em aberto.");
			}
		});
		
		repasse.setDataDoPagamento(LocalDate.now());
		this.update(repasse);
		
		this.realizarLancamentosContabeisDeRepasseMensal(operacao, repasse);
		
		String historico = "Resgate - " + operacao.getInvestidorSenior().getPessoa().getNome() + " referente ao repasse número " +
				repasse.getNumero() + "("+ Util.formatLocalDateToString(repasse.getVencimentoAtual()) +")" +  " da operação de número "  + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".";
		
		BigDecimal valorResgate = repasse.getValor();
		
		this.investidorService.resgate(operacao.getInvestidor(), valorResgate, repasse.getContaCorrente(),
				operacao.getInvestidorSenior(), historico, repasse.getDataDoPagamento());
		
		if (repasse.getGerarProximoRepasse()) {
			boolean existeParcelaProResgate = !operacao.getParcelas().stream().allMatch(parcela -> parcela.getDataDoPagamento() != null);		
			if (!existeParcelaProResgate) {
				throw new BaseException("Próximo repasse não pôde ser criado. Não existe parcela em aberto.");
			}
			repasse.setOperacao(operacao);
			if (operacao.isPagamentoSomenteJuros()) {
				this.gerarRepasseMensalParaOperacaoSomenteJuros(operacao);
			}
		}	
		
	}
	
	private void realizarLancamentosContabeisDeRepasseMensal(Operacao operacao, RepasseMensal repasse) {
		TipoLancamentoContabil tipoPagamentoInvestidorSenior = new TipoLancamentoContabil("Pagamento de Investidor Senior");
		tipoPagamentoInvestidorSenior = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoPagamentoInvestidorSenior)).get();
		
		ContaContabil contaCapitalInvestidorSenior = new ContaContabil();
		contaCapitalInvestidorSenior.setNome(ContaContabilService.CONTA_CAPITAL + operacao.getInvestidorSenior().getPessoa().getNome());
		contaCapitalInvestidorSenior = this.contaContabilRepository.findOne(Example.of(contaCapitalInvestidorSenior)).get();
		
		ContaContabil contaDespesa = new ContaContabil();
		contaDespesa.setNome(ContaContabilService.CONTA_DESPESA + operacao.getInvestidor().getPessoa().getNome());
		contaDespesa = this.contaContabilRepository.findOne(Example.of(contaDespesa)).get();
		
		this.contaContabilService.creditar(contaCapitalInvestidorSenior, contaDespesa, repasse.getAgio(), tipoPagamentoInvestidorSenior,
				"Pagamento de Investidor Senior referente ao repasse número " + repasse.getNumero() + "("+ Util.formatLocalDateToString(repasse.getVencimentoAtual()) +")" +  " da operação de número " + operacao.getNumero() + " do(a) cliente " + operacao.getCliente().getPessoa().getNome() + ".",
				repasse.getDataDoPagamento(), operacao.getResponsavel());
	}
	
	public void gerarRepasseMensalParaOperacaoSomenteJuros(Operacao operacao) {
		RepasseMensal novoRepasse = new RepasseMensal();
		RepasseMensal ultimoRepasse = operacao.getRepassesMensais().get(operacao.getRepassesMensais().size()-1);
		
		novoRepasse.setDataDeVencimento(ultimoRepasse.getVencimentoAtual().plusMonths(1));
		novoRepasse.setNumero(ultimoRepasse.getNumero()+1);
		novoRepasse.setOperacao(operacao);
		novoRepasse.setValor(this.calcularValorDoRepasse(operacao));
		novoRepasse.setAgio(novoRepasse.getValor());
		
		this.persist(novoRepasse);
	}
	
	public RepasseMensal gerarRepasseMensalParaPagamentoSomenteJuros(Parcela parcela) {
		Operacao operacao = parcela.getOperacao();
		
		RepasseMensal novoRepasse = new RepasseMensal();
		
		String historico = "Repasse mensal criado referente ao pagamento de somente o juros operação " +
				operacao.getId() + ".";
		
		novoRepasse.setDataDeVencimento(parcela.getDataDoPagamento());
		novoRepasse.setHistorico(historico);
		novoRepasse.setNumero(operacao.getRepassesMensais().size() + 1);
		novoRepasse.setOperacao(operacao);
		novoRepasse.setValor(this.calcularValorDoRepasseSomenteJuros(operacao));
		novoRepasse.setAgio(novoRepasse.getValor());
		
		return this.persist(novoRepasse);
	}

	public void ajustarRepassesAposPagamentoSomenteJuros(RepasseMensal repasseQuePagouSomenteJuros) {
	    Operacao operacao = this.operacaoRepository.findById(repasseQuePagouSomenteJuros.getCodigoOperacao()).get();
	    
	    operacao.getRepassesMensais().forEach( repasse -> {
	    	if (!repasse.equals(repasseQuePagouSomenteJuros) && repasse.getDataDoPagamento() == null) {
				LocalDate dataDeProrrogacao = repasse.getVencimentoAtual().plusMonths(1);
				repasse.historico(
						"Repasse mensal prorrogado de " + Util.formatLocalDateToString(repasse.getVencimentoAtual()) + 
						" para " + Util.formatLocalDateToString(dataDeProrrogacao) + " devido pagamento de somente o juros da operação.");
				repasse.setDataDeProrrogacao(dataDeProrrogacao);
				this.update(repasse);	    		
	    	}	    	
	    });
	}	
}
