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
import br.com.rmc.cepf.cliente.Cliente;
import br.com.rmc.cepf.conta.ContaCorrente;

@Entity
@Table(schema="emprestimo")
public class Parcela extends BaseEntity {
	
	private static final long serialVersionUID = -7450608560390330200L;
	
	public Parcela() {
	}
	
	public Parcela(Integer numero) {
		this.numero = numero;
	}
	
	@NotNull
	@Column(nullable = false)
	private Integer numero;
	
	@NotNull
	@Column(name = "data_vencimento", nullable = false, columnDefinition = "DATE")
	private LocalDate dataDeVencimento;
	
	@Column(name = "data_prorrogacao", columnDefinition = "DATE")
	private LocalDate dataDeProrrogacao;
	
	@NotNull
	@Column(nullable = false)	
	private BigDecimal valor;
	
	@Column(name = "data_pagamento", columnDefinition = "DATE")
	private LocalDate dataDoPagamento;
	
	@Column(name = "data_cancelamento", columnDefinition = "DATE")
	private LocalDate dataDeCancelamento;	
	
	@Column(name = "valor_pago")	
	private BigDecimal valorPago;
	
	@NotNull
	@Column(nullable = false)	
	private BigDecimal receita;	
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Cliente cliente;
	
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	@JoinColumn(nullable = false, name = "operacao_id", updatable = false)
	private Operacao operacao;
	
	@ManyToOne
	@JoinColumn(name = "conta_corrente_id")
	private ContaCorrente contaDeDeposito;
	
	@Column(length = 5000)
	private String historico;
	
	@Transient
	private Long codigoOperacao;
	
	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean gerarProximaParcela;
	
	@Transient
	public Boolean isVencendo() {
		return getVencimentoAtual().equals(LocalDate.now());
	}
	
	@Transient
	public Boolean isVencida() {
		return LocalDate.now().isAfter(getVencimentoAtual());
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
	
	public LocalDate getDataDeProrrogacao() {
		return dataDeProrrogacao;
	}

	public void setDataDeProrrogacao(LocalDate dataDeProrrogacao) {
		this.dataDeProrrogacao = dataDeProrrogacao;
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
	
	public LocalDate getDataDeCancelamento() {
		return dataDeCancelamento;
	}

	public void setDataDeCancelamento(LocalDate dataDeCancelamento) {
		this.dataDeCancelamento = dataDeCancelamento;
	}	
	
	public BigDecimal getValorPago() {
		return valorPago;
	}
	
	public BigDecimal getReceita() {
		return receita;
	}

	public void setReceita(BigDecimal receita) {
		this.receita = receita;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public ContaCorrente getContaDeDeposito() {
		return contaDeDeposito;
	}

	public void setContaDeDeposito(ContaCorrente contaDeDeposito) {
		this.contaDeDeposito = contaDeDeposito;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public Long getCodigoOperacao() {
		return this.operacao != null && this.operacao.getId() != null ? 
				this.operacao.getId() : this.codigoOperacao != null ? this.codigoOperacao : null;
	}

	public void setCodigoOperacao(Long codigoOperacao) {
		this.codigoOperacao = codigoOperacao;
	}

	public boolean getGerarProximaParcela() {
		return gerarProximaParcela;
	}

	public void setGerarProximaParcela(boolean gerarProximaParcela) {
		this.gerarProximaParcela = gerarProximaParcela;
	}
	
}