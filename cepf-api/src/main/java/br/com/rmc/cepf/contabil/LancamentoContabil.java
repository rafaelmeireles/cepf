package br.com.rmc.cepf.contabil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.rmc.BaseEntity;
import br.com.rmc.BaseException;
import br.com.rmc.security.user.User;

@Entity
@Table(schema="contabil", name="lancamento_contabil")
//@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LancamentoContabil extends BaseEntity {
	
	private static final long serialVersionUID = -6366894942318374851L;
	
	public LancamentoContabil() {
	}
	
	public LancamentoContabil(ContaContabil contaContabil) {
		this.data = LocalDateTime.now();
		this.conta = contaContabil;
		this.saldoAnteriorDaConta = contaContabil.getSaldo();
	}	
	
	@NotNull
	@Column(nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime data;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "TIMESTAMP", name = "data_referencia")
	private LocalDate dataDeReferencia;
	
	@NotNull
	@Column(nullable = false)	
	private String historico;
	
	@NotNull
	@OneToOne
	@JoinColumn(nullable = false, updatable = false)
	private TipoLancamentoContabil tipoLancamentoContabil;
	
	@NotNull
	@OneToOne
	@JoinColumn(nullable = false)	
	private User responsavel;
	
	@NotNull
	@Column(scale = 2, nullable = false)
	private BigDecimal valor;
	
	private Boolean credito;
	
	private Boolean debito;
	
	@NotNull
	@OneToOne
	@JoinColumn(nullable = false, updatable = false)
	private ContaContabil conta;
	
//	@JsonIgnore
	@JoinColumn
	@OneToOne(cascade = CascadeType.ALL)
	@JsonProperty(access = Access.WRITE_ONLY)
	private LancamentoContabil contraPartida;
	
//	private LancamentoContabil lancamentoEstorno;
	
	@NotNull
	@Column(scale = 2, nullable = false)
	private BigDecimal saldoAnteriorDaConta;

	@Override
	public void validate() throws BaseException {
		if (getResponsavel() == null) {
			throw new BaseException("Falha ao validar um Lançamento no sistema. O Lançamento informado não possui um responsável.");
		}
		
		if (getCredito() == null && getDebito() == null) {
			throw new BaseException("Falha ao validar um Lançamento no sistema. Não foi informado se o lançamento é de crédito ou débito.");
		}
		
		if (getCredito() != null && getDebito() != null) {
			throw new BaseException("Falha ao validar um Lançamento no sistema. Somente uma operação é permitida.");
		}
	}
	
	@Transient
	public Boolean isDebito() {
		return getDebito() != null && getDebito();
	}
	
	@Transient
	public Boolean isCredito() {
		return getCredito() != null && getCredito();
	}
	
	@Transient
	@JsonIgnore
	public LancamentoContabil credito(BigDecimal valor) {
		setValor(valor);
		setCredito(true);
		setDebito(false);
		return this;
	}
	
	@Transient
	@JsonIgnore
	public LancamentoContabil debito(BigDecimal valor) {
		setValor(valor);
		setCredito(false);
		setDebito(true);
		return this;
	}	
	
	@Transient
	@JsonIgnore
	public LancamentoContabil tipo(TipoLancamentoContabil tipoLancamentoContabil) {
		setTipoLancamentoContabil(tipoLancamentoContabil);
		return this;
	}
	
	@Transient
	@JsonIgnore
	public LancamentoContabil historico(String historico) {
		setHistorico(historico);
		return this;
	}

	@Transient
	@JsonIgnore
	public LancamentoContabil dataReferencia(LocalDate data) {
		setDataDeReferencia(data);
		return this;
	}	
	
	@Transient
	@JsonIgnore
	public LancamentoContabil responsavel(User user) {
		setResponsavel(user);
		return this;
	}
	
	@Transient
	public String getContaContraPartida() {
		return getContraPartida().getConta().getId() + " - " + getContraPartida().getConta().getNome();
	}
	
	//GETs e SETs
	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public LocalDate getDataDeReferencia() {
		return dataDeReferencia;
	}

	public void setDataDeReferencia(LocalDate dataDeReferencia) {
		this.dataDeReferencia = dataDeReferencia;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public TipoLancamentoContabil getTipoLancamentoContabil() {
		return tipoLancamentoContabil;
	}

	public void setTipoLancamentoContabil(TipoLancamentoContabil tipoLancamentoContabil) {
		this.tipoLancamentoContabil = tipoLancamentoContabil;
	}

	public User getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(User responsavel) {
		this.responsavel = responsavel;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Boolean getCredito() {
		return credito;
	}

	public void setCredito(Boolean credito) {
		this.credito = credito;
	}

	public Boolean getDebito() {
		return debito;
	}

	public void setDebito(Boolean debito) {
		this.debito = debito;
	}

	public ContaContabil getConta() {
		return conta;
	}

	public void setConta(ContaContabil conta) {
		this.conta = conta;
	}

	public LancamentoContabil getContraPartida() {
		return contraPartida;
	}

	public void setContraPartida(LancamentoContabil contraPartida) {
		this.contraPartida = contraPartida;
	}

	public BigDecimal getSaldoAnteriorDaConta() {
		return saldoAnteriorDaConta;
	}

	public void setSaldoAnteriorDaConta(BigDecimal saldoAnteriorDaConta) {
		this.saldoAnteriorDaConta = saldoAnteriorDaConta;
	}
}