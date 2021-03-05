package br.com.rmc.cepf.conta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import br.com.rmc.BaseService;
import br.com.rmc.cepf.contabil.ContaContabil;
import br.com.rmc.cepf.contabil.ContaContabilRepository;
import br.com.rmc.cepf.contabil.ContaContabilService;
import br.com.rmc.cepf.contabil.TipoLancamentoContabil;
import br.com.rmc.cepf.contabil.TipoLancamentoContabilRepository;

@Service
public class TransferenciaService extends BaseService<Transferencia> {
	
	private static final long serialVersionUID = -681678049203666599L;
	
	@Autowired
	private ContaContabilRepository contaContabilRepository;	
	
	@Autowired
	private TipoLancamentoContabilRepository tipoLancamentoContabilRepository;		
	
	@Autowired
	private ContaContabilService contaContabilService;	

	public TransferenciaService(TransferenciaRepository repository) {
		super(repository);
	}
	
	@Override
	public Transferencia persist(Transferencia transferencia) {
		transferencia = super.persist(transferencia);
		this.realizarLancamentosContabeis(transferencia);
		return transferencia;
	}
	
	private void realizarLancamentosContabeis(Transferencia transferencia) {
		TipoLancamentoContabil tipoLancamentoContabil = new TipoLancamentoContabil("Transferencia entre contas");
		tipoLancamentoContabil = this.tipoLancamentoContabilRepository.findOne(Example.of(tipoLancamentoContabil)).get();
		
		ContaContabil contaCliente = new ContaContabil();
		contaCliente.setNome(ContaContabilService.CONTA_CLIENTE + transferencia.getFavorecido().getCliente().getPessoa().getNome());
		contaCliente = this.contaContabilRepository.findOne(Example.of(contaCliente)).get();
		
		this.contaContabilService.debitar(contaCliente, transferencia.getContaOrigem().getContaContabil(), transferencia.getValor(),
				tipoLancamentoContabil, "Transferencia entre contas", transferencia.getData().toLocalDate(), transferencia.getResponsavel());
	}
}
