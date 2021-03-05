package br.com.rmc.cepf.contabil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;


public class LancamentoContabilDTO implements Serializable {
	
	private static final long serialVersionUID = -6366894942318374851L;
	
	public LancamentoContabilDTO() {
	}
	
	private Long id;
	private Date data;	
	private String historico;
	private String tipoLancamentoContabil;
	private BigDecimal valor;
	private Boolean credito;
	private Boolean debito;
	private String contraPartida;
	
	@Transient
	public Boolean isDebito() {
		return getDebito() != null && getDebito();
	}
	
	@Transient
	public Boolean isCredito() {
		return getCredito() != null && getCredito();
	}
	
	@Transient
	public LancamentoContabilDTO credito(BigDecimal valor) {
		setValor(valor);
		setCredito(true);
		setDebito(false);
		return this;
	}
	
	@Transient
	public LancamentoContabilDTO debito(BigDecimal valor) {
		setValor(valor);
		setCredito(false);
		setDebito(true);
		return this;
	}	
	
	@Transient
	public LancamentoContabilDTO tipo(String tipoLancamentoContabil) {
		setTipoLancamentoContabil(tipoLancamentoContabil);
		return this;
	}
	
	@Transient
	public LancamentoContabilDTO historico(String historico) {
		setHistorico(historico);
		return this;
	}

	//GETs e SETs	
	public Long getId() {
		return id;
	}

	public LancamentoContabilDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public Date getData() {
		return data;
	}

	public LancamentoContabilDTO setData(Date data) {
		this.data = data;
		return this;
	}

	public String getHistorico() {
		return historico;
	}

	public LancamentoContabilDTO setHistorico(String historico) {
		this.historico = historico;
		return this;
	}

	public String getTipoLancamentoContabil() {
		return tipoLancamentoContabil;
	}

	public LancamentoContabilDTO setTipoLancamentoContabil(String tipoLancamentoContabil) {
		this.tipoLancamentoContabil = tipoLancamentoContabil;
		return this;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public LancamentoContabilDTO setValor(BigDecimal valor) {
		this.valor = valor;
		return this;
	}

	public Boolean getCredito() {
		return credito;
	}

	public LancamentoContabilDTO setCredito(Boolean credito) {
		this.credito = credito;
		return this;
	}

	public Boolean getDebito() {
		return debito;
	}

	public LancamentoContabilDTO setDebito(Boolean debito) {
		this.debito = debito;
		return this;
	}

	public String getContraPartida() {
		return contraPartida;
	}

	public LancamentoContabilDTO setContraPartida(String contraPartida) {
		this.contraPartida = contraPartida;
		return this;
	}
}