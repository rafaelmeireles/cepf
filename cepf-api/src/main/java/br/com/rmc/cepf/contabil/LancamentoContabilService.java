package br.com.rmc.cepf.contabil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.rmc.BaseService;
import br.com.rmc.security.user.User;
import br.com.rmc.specification.DateTimeSpecification;
import br.com.rmc.specification.JoinSpecification;

@Service
public class LancamentoContabilService extends BaseService<LancamentoContabil> {
	
	@Autowired
	private ContaContabilService contaContabilService;	
	
	private static final long serialVersionUID = -681678049203666599L;

	public LancamentoContabilService(LancamentoContabilRepository repository) {
		super(repository);
	}

	@Override
	protected LancamentoContabilRepository getRepository() {
		return (LancamentoContabilRepository) super.getRepository();
	}

	public List<LancamentoContabil> find(LancamentoContabil lancamentoContabil, LocalDateTime dataInicial, LocalDateTime dataFinal) {
		return getRepository().findAll(
				Specification
				.where(new JoinSpecification<LancamentoContabil, ContaContabil>("conta", lancamentoContabil.getConta()))
				.and(new JoinSpecification<LancamentoContabil, TipoLancamentoContabil>("tipoLancamentoContabil", lancamentoContabil.getTipoLancamentoContabil()))
				.and(new DateTimeSpecification<LancamentoContabil>("data", dataInicial, dataFinal)),
				Sort.by(Direction.ASC, "data")
				);
	}

	public void lancamentoManual(LancamentoContabil lancamentoContabil) {
		ContaContabil contaDeCredito = lancamentoContabil.getConta();
		ContaContabil contraDeDebito = lancamentoContabil.getContraPartida().getConta();
		BigDecimal valor = lancamentoContabil.getValor();
		TipoLancamentoContabil tipoLancamento = lancamentoContabil.getTipoLancamentoContabil();
		String historico = lancamentoContabil.getHistorico();
		LocalDate dataDeReferencia = lancamentoContabil.getDataDeReferencia();
		User responsavel = lancamentoContabil.getResponsavel();
		
		this.contaContabilService.creditar(contaDeCredito, contraDeDebito, valor,
				tipoLancamento, historico, dataDeReferencia, responsavel);
	}
}
