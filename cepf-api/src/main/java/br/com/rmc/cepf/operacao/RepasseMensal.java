package br.com.rmc.cepf.operacao;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.rmc.BaseEntity;
import br.com.rmc.cepf.conta.ContaCorrente;

@Entity(name = "repasse_mensal")
@Table(schema="emprestimo")
public class RepasseMensal extends BaseEntity {
	
	private static final long serialVersionUID = -596325433359936625L;
	
	@NotNull
	@Column(nullable = false)
	private Integer numero;	

	@NotNull
	@Column(name = "data_vencimento", nullable = false, columnDefinition = "DATE")
	private LocalDate dataDeVencimento;
	
	@NotNull
	@Column(nullable = false)	
	private BigDecimal valor;
	
	@Column(name = "data_pagamento", columnDefinition = "DATE")
	private LocalDate dataDoPagamento;
	
	@Column(name = "data_prorrogacao", columnDefinition = "DATE")
	private LocalDate dataDeProrrogacao;
	
	@Column(name = "data_cancelamento", columnDefinition = "DATE")
	private LocalDate dataDeCancelamento;
	
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	@JoinColumn(nullable = false, name = "operacao_id", updatable = false)
	private Operacao operacao;
	
	@NotNull
	@Column(nullable = false)
	private BigDecimal agio;
	
	@Column	
	private String historico;	
	
	@Transient
	private Long codigoOperacao;
	
	@Transient
	private ContaCorrente contaCorrente;
	
	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean gerarProximoRepasse;	
	
	@Transient
	public Boolean isVencendo() {
		return getDataDoPagamento() == null && LocalDate.now().equals(getVencimentoAtual());
	}
	
	@Transient
	public Boolean isVencido() {
		return getDataDoPagamento() == null && LocalDate.now().isAfter(getVencimentoAtual());
	}
	
	@Transient
	@JsonIgnore
	public void historico(String historico) {
		historico = (getHistorico() == null ? "" : getHistorico()) + historico;
		setHistorico(historico + System.getProperty("line.separator"));
	}
	
	@Transient
	public LocalDate getVencimentoAtual() {
		return getDataDeProrrogacao() != null ? getDataDeProrrogacao() : getDataDeVencimento();
	}	
	
	//GETs e SETs
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public LocalDate getDataDeVencimento() {
		return dataDeVencimento;
	}
	
	public void setDataDeVencimento(LocalDate dataDeVencimento) {
		this.dataDeVencimento = dataDeVencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
		
	public LocalDate getDataDoPagamento() {
		return dataDoPagamento;
	}
	
	public void setDataDoPagamento(LocalDate dataDoPagamento) {
		this.dataDoPagamento = dataDoPagamento;
	}
	
	public LocalDate getDataDeProrrogacao() {
		return dataDeProrrogacao;
	}

	public void setDataDeProrrogacao(LocalDate dataDeProrrogacao) {
		this.dataDeProrrogacao = dataDeProrrogacao;
	}
	
	public LocalDate getDataDeCancelamento() {
		return dataDeCancelamento;
	}

	public void setDataDeCancelamento(LocalDate dataDeCancelamento) {
		this.dataDeCancelamento = dataDeCancelamento;
	}	

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public Long getCodigoOperacao() {
		return this.operacao != null ? this.operacao.getId() : this.codigoOperacao != null ? this.codigoOperacao : null;
	}

	public void setCodigoOperacao(Long codigoOperacao) {
		this.codigoOperacao = codigoOperacao;
	}

	public BigDecimal getAgio() {
		return agio;
	}

	public void setAgio(BigDecimal agio) {
		this.agio = agio;
	}

	public ContaCorrente getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public boolean getGerarProximoRepasse() {
		return gerarProximoRepasse;
	}

	public void setGerarProximoRepasse(boolean gerarProximoRepasse) {
		this.gerarProximoRepasse = gerarProximoRepasse;
	}
	
	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}	
	
}