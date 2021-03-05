package br.com.rmc.cepf.investidor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.rmc.BaseException;
import br.com.rmc.BaseService;
import br.com.rmc.cepf.conta.ContaCorrente;
import br.com.rmc.cepf.contabil.ContaContabil;
import br.com.rmc.cepf.contabil.ContaContabilRepository;
import br.com.rmc.cepf.contabil.ContaContabilService;
import br.com.rmc.cepf.contabil.TipoLancamentoContabil;
import br.com.rmc.cepf.contabil.TipoLancamentoContabilRepository;
import br.com.rmc.security.UserApplication;
import br.com.rmc.security.UserApplicationRepository;

@Service
public class InvestidorService extends BaseService<Investidor> {

	private static final long serialVersionUID = 4044055203125228964L;

	@Autowired
	private UserApplicationRepository userApplicationRepository;

	@Autowired
	private ContaContabilRepository contaContabilRepository;

	@Autowired
	private TipoLancamentoContabilRepository tipoLancamentoContabilRepository;	

	@Autowired
	private ContaContabilService contaContabilService;

	public InvestidorService(InvestidorRepository repository) {
		super(repository);
	}

	@Override
	public Investidor persist(Investidor investidor) {

		if (repository.exists(Example.of(investidor))) {
			throw new BaseException("Já existe um investidor cadastrado para o CPF " + investidor.getPessoa().getCpf() + " .");
		}

		this.criarContas(investidor.getPessoa().getNome(), investidor.getContas());

		return super.persist(investidor);
	}
	
	private void criarContas(String nome, List<ContaCorrente> contas) {
		
		contas.stream().forEach(contaCorrente -> {
			contaCorrente.descricao(nome);
			
			String nomeConta = ContaContabilService.CONTA_CORRENTE + nome  + " - " +
					contaCorrente.getBanco().getNome() + " - " + contaCorrente.getConta() + contaCorrente.getDigitoConta().toString();
			
			contaCorrente.setContaContabil(new ContaContabil().nome(nomeConta).devedora().zeraSaldo());
			this.contaContabilService.persist(contaCorrente.getContaContabil());
		});		
		
		this.contaContabilService.persist(new ContaContabil().nome(ContaContabilService.CONTA_CAPITAL + nome).credora().zeraSaldo());
		this.contaContabilService.persist(new ContaContabil().nome(ContaContabilService.CONTA_RECEITA_A_RECEBER + nome).credora().zeraSaldo());
		this.contaContabilService.persist(new ContaContabil().nome(ContaContabilService.CONTA_RECEITA_RECEBIDA + nome).credora().zeraSaldo());
		this.contaContabilService.persist(new ContaContabil().nome(ContaContabilService.CONTA_DESPESA + nome).devedora().zeraSaldo());
//		persist(new ContaContabil().nome(ContaContabilService.CONTA_DESPESA_BANCARIA + nome).devedora().zeraSaldo());
//		persist(new ContaContabil().nome(ContaContabilService.CONTA_DESPESA_COM_INVESTIDOR_SENIOR + nome).devedora().zeraSaldo());
		this.contaContabilService.persist(new ContaContabil().nome(ContaContabilService.CONTA_PARCELAS_A_RECEBER + nome).devedora().zeraSaldo());
		this.contaContabilService.persist(new ContaContabil().nome(ContaContabilService.CONTA_PARCELAS_A_RECEBER_SOMENTE_JUROS + nome).devedora().zeraSaldo());
		this.contaContabilService.persist(new ContaContabil().nome(ContaContabilService.CONTA_LUCROS_AH_DISTRIBUIR + nome).credora().zeraSaldo());
		this.contaContabilService.persist(new ContaContabil().nome(ContaContabilService.CONTA_PROVISAO_DE_PERDAS + nome).credora().zeraSaldo());
	}	

	@Override
	protected void validateRequireds(Investidor investidor) {
		Assert.notNull(investidor.getPessoa(), "Campo obrigatório 'pessoa' não informado");
		Assert.notNull(investidor.getPessoa().getNome(), "Campo obrigatório 'nome' não informado");
		Assert.notNull(investidor.getPessoa().getTelefonePrincipal(), "Campo obrigatório 'telefone principal' não informado");
		Assert.notNull(investidor.getContas(), "Campo obrigatório 'conta bancária' não informado");
		Assert.notEmpty(investidor.getContas(), "Campo obrigatório 'conta bancária' não informado");
	}

	public void aporte(Investidor investidor, BigDecimal valor, ContaCorrente contaDoDeposito, Investidor investidorSenior) {

		Assert.isTrue(investidor != null || investidorSenior != null, "Nenhum investidor foi informado para efetuar o resgate.");
		Assert.notNull(valor, "Campo obrigatório 'valor' não informado.");
		Assert.notNull(contaDoDeposito, "Campo obrigatório 'conta para deposito' não informado.");
		Assert.notNull(contaDoDeposito.getContaContabil(), "Campo obrigatório 'conta para deposito' não informado.");		

		UserApplication user = new UserApplication();
		user.setInvestidor(investidor);
		user = this.userApplicationRepository.findOne(Example.of(user)).get();

		TipoLancamentoContabil tipoLancamentoContabil = new TipoLancamentoContabil("Aporte");
		tipoLancamentoContabil = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoLancamentoContabil)).get();

		ContaContabil contaCapital = new ContaContabil();
		String historico = "Aporte - ";

		if (investidorSenior != null && investidorSenior.getId() != null) {
			contaCapital.nome(ContaContabilService.CONTA_CAPITAL + investidorSenior.getPessoa().getNome()).credora();
			historico += investidorSenior.getPessoa().getNome();
		} else {
			contaCapital.nome(ContaContabilService.CONTA_CAPITAL + investidor.getPessoa().getNome()).credora();
			historico += investidor.getPessoa().getNome();
		}
		contaCapital = this.contaContabilRepository.findOne(Example.of(contaCapital)).get();

		this.contaContabilService.creditar(contaCapital, contaDoDeposito.getContaContabil(),
				valor, tipoLancamentoContabil, historico, LocalDate.now(), user);
	}
	
	public void resgate(Investidor investidor, BigDecimal valor, ContaCorrente contaCorrente,
			Investidor investidorSenior) {
		
		String historico = "Resgate - ";
		if (investidorSenior != null && investidorSenior.getId() != null) {
			historico += investidorSenior.getPessoa().getNome();
		} else {
			historico += investidor.getPessoa().getNome();
		}
		
		resgate(investidor, valor, contaCorrente, investidorSenior, historico, LocalDate.now());
	}

	public void resgate(Investidor investidor, BigDecimal valor, ContaCorrente contaCorrente,
			Investidor investidorSenior, String historico, LocalDate dataDeReferencia) {

		Assert.isTrue(investidor != null || investidorSenior != null, "Nenhum investidor foi informado para efetuar o resgate.");
		Assert.notNull(valor, "Campo obrigatório 'valor' não informado.");
		Assert.notNull(contaCorrente, "Campo obrigatório 'conta corrente' não informado.");
		Assert.notNull(contaCorrente.getContaContabil(), "Campo obrigatório 'conta corrente' não informado.");

		UserApplication user = new UserApplication();
		user.setInvestidor(investidor);
		user = this.userApplicationRepository.findOne(Example.of(user)).get();

		TipoLancamentoContabil tipoLancamentoContabil = new TipoLancamentoContabil("Resgate");
		tipoLancamentoContabil = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoLancamentoContabil)).get();

		ContaContabil contaCapital = new ContaContabil();

		if (investidorSenior != null && investidorSenior.getId() != null) {
			contaCapital.nome(ContaContabilService.CONTA_CAPITAL + investidorSenior.getPessoa().getNome()).credora();
//			historico += investidorSenior.getPessoa().getNome();
		} else {
			contaCapital.nome(ContaContabilService.CONTA_CAPITAL + investidor.getPessoa().getNome()).credora();
//			historico += investidor.getPessoa().getNome();
		}
		contaCapital = this.contaContabilRepository.findOne(Example.of(contaCapital)).get();

		this.contaContabilService.debitar(contaCapital, contaCorrente.getContaContabil(),
				valor, tipoLancamentoContabil, historico, dataDeReferencia, user);
	}

	public void apuracaoResultado(Investidor investidor, LocalDate periodoInicial, LocalDate periodoFinal) {
		
		Assert.isTrue(periodoInicial != null && periodoFinal != null, "O período de apuração deve ser informado.");
		
		TipoLancamentoContabil tipoApuracaoResultado = new TipoLancamentoContabil("Apuração de Resultado");
		tipoApuracaoResultado = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoApuracaoResultado)).get();
		
		TipoLancamentoContabil tipoProvisaoDePerdas = new TipoLancamentoContabil("Provisão de Perdas");
		tipoProvisaoDePerdas = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoProvisaoDePerdas)).get();		

		TipoLancamentoContabil tipoDistribuicaoResultado = new TipoLancamentoContabil("Distribuição de Resultado");
		tipoDistribuicaoResultado = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoDistribuicaoResultado)).get();

		ContaContabil contaLucrosAhDistribuir = new ContaContabil();
		contaLucrosAhDistribuir.setNome(ContaContabilService.CONTA_LUCROS_AH_DISTRIBUIR + investidor.getPessoa().getNome());
		contaLucrosAhDistribuir = this.contaContabilRepository.findOne(Example.of(contaLucrosAhDistribuir)).get();

		ContaContabil contaReceitaRecebida = new ContaContabil();
		contaReceitaRecebida.setNome(ContaContabilService.CONTA_RECEITA_RECEBIDA + investidor.getPessoa().getNome());
		contaReceitaRecebida = this.contaContabilRepository.findOne(Example.of(contaReceitaRecebida)).get();

		ContaContabil contaDespesa = new ContaContabil();
		contaDespesa.setNome(ContaContabilService.CONTA_DESPESA + investidor.getPessoa().getNome());
		contaDespesa = this.contaContabilRepository.findOne(Example.of(contaDespesa)).get();
		
		ContaContabil contaProvisaoPerdas = new ContaContabil();
		contaProvisaoPerdas.setNome(ContaContabilService.CONTA_PROVISAO_DE_PERDAS + investidor.getPessoa().getNome());
		contaProvisaoPerdas = this.contaContabilRepository.findOne(Example.of(contaProvisaoPerdas)).get();		


		ContaContabil contaCapital = new ContaContabil();
		contaCapital.nome(ContaContabilService.CONTA_CAPITAL + investidor.getPessoa().getNome()).credora();
		contaCapital = this.contaContabilRepository.findOne(Example.of(contaCapital)).get();
		
		UserApplication user = new UserApplication();
		user.setInvestidor(investidor);
		user = this.userApplicationRepository.findOne(Example.of(user)).get();
		
		BigDecimal receita = this.contaContabilService.getTotalDeEntradas(contaReceitaRecebida, periodoInicial, periodoFinal);
		BigDecimal despesa = this.contaContabilService.getTotalDeEntradas(contaDespesa, periodoInicial, periodoFinal);
		BigDecimal porcentagemProvisaoDePerdas = new BigDecimal("10").divide(new BigDecimal("100"));//10% para perdas
		BigDecimal provisaoDePerdas = receita.multiply(porcentagemProvisaoDePerdas);
		
		LocalDate dataApuracao = periodoFinal;
		LocalDate dataDistribuicao = periodoFinal.plusDays(1);
		
		this.contaContabilService.creditar(contaLucrosAhDistribuir, contaReceitaRecebida,
				receita, tipoApuracaoResultado, tipoApuracaoResultado.getNome(), dataApuracao, user);
		
		this.contaContabilService.debitar(contaLucrosAhDistribuir, contaDespesa, despesa,
				tipoApuracaoResultado, tipoApuracaoResultado.getNome(), dataApuracao, user);
		
		this.contaContabilService.debitar(contaLucrosAhDistribuir, contaProvisaoPerdas, provisaoDePerdas,
				tipoProvisaoDePerdas, tipoProvisaoDePerdas.getNome(), dataDistribuicao, user);
		
		this.contaContabilService.debitar(contaLucrosAhDistribuir, contaCapital, receita.subtract(despesa).subtract(provisaoDePerdas),
				tipoDistribuicaoResultado, tipoDistribuicaoResultado.getNome(), dataDistribuicao, user);

	}
}
