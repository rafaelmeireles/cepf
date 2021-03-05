package br.com.rmc.cepf.operacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.rmc.BaseEntity;
import br.com.rmc.cepf.cliente.Cliente;
import br.com.rmc.cepf.conta.ContaCorrente;
import br.com.rmc.cepf.conta.Transferencia;
import br.com.rmc.cepf.investidor.Investidor;
import br.com.rmc.security.UserApplication;

@Entity
@Table(schema="emprestimo", name = "operacao")
public class Operacao extends BaseEntity {
	
	private static final long serialVersionUID = -2099438775357986486L;
	
	@NotNull
	@Column(nullable = false)
	private Integer numero;
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Cliente cliente;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "DATE")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	@Column(name = "data_autorizacao")
	private LocalDateTime dataDeAutorizacao;
	
	@Column(name = "data_finalizacao")
	private LocalDateTime dataFinalizacao;
	
	@Column(name = "data_cancelamento", columnDefinition = "DATE")
	private LocalDate dataDeCancelamento;	
	
	@NotNull
	@Column(nullable = false)	
	private BigDecimal valor;
	
	@NotNull
	@Column(nullable = false)	
	private BigDecimal taxa;
	
	@NotNull
	@Column(nullable = false)	
	private Integer quantidadeDeParcelas;	
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)	
	private Investidor investidor;
	
	@NotNull
	@Column(nullable = false, name = "tipo_operacao")
	@Enumerated(EnumType.STRING)	
	private TipoDeOperacao tipoDeOperacao;
	
	@ManyToOne
	@JoinColumn
	private Investidor investidorSenior;
	
	@Column(name = "taxa_repasse")
	private BigDecimal taxaDeRepasse;
	
	@NotNull
	@Column(name = "tipo_pagamento", nullable = false)
	@Enumerated(EnumType.STRING)	
	private TipoDePagamento tipoDePagamento;
	
	@NotNull
	@OneToOne
	@JoinColumn(nullable = false)	
	private UserApplication responsavel;
	
	@JoinColumn
	@OneToOne(cascade = CascadeType.ALL)
	private Transferencia transferencia;
	
    @OrderBy(value = "dataDeVencimento ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "operacao")
	private List<Parcela> parcelas;
    
    @OrderBy(value = "dataDeVencimento ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "operacao")
	private List<RepasseMensal> repassesMensais;    
    
    @Transient
    @JsonProperty(access = Access.WRITE_ONLY)
    private ContaCorrente contaCorrentePagamentoSomenteJuros;
    
    @Transient
    @JsonProperty(access = Access.WRITE_ONLY)
    private ContaCorrente contaCorrentePagamentoDoRepasseSomenteJuros;

    @Transient
    @JsonProperty(access = Access.WRITE_ONLY)
    private LocalDate dataDePagamentoSomenteJuros;
    
	@Column	
	private String historico;
	
	@Transient
	public boolean isRepasse() {
		return TipoDeOperacao.REPASSE.equals(getTipoDeOperacao());
	}
	
	@Transient
	public boolean isPagamentoSemanal() {
		return TipoDePagamento.SEMANAL.equals(getTipoDePagamento());
	}
	
	@Transient
	public boolean isPagamentoQuinzenal() {
		return TipoDePagamento.QUINZENAL.equals(getTipoDePagamento());
	}
	
	@Transient
	public boolean isPagamentoMensal() {
		return TipoDePagamento.MENSAL.equals(getTipoDePagamento());
	}
	
	@Transient
	public boolean isPagamentoSomenteJuros() {
		return TipoDePagamento.SOMENTE_JUROS.equals(getTipoDePagamento());
	}
	
	@Transient
	public boolean isTemRepasseMensal() {
		
		if (isCancelada() || !isRepasse()) {
			return false;
		}
		
		for (RepasseMensal repasse : getRepassesMensais()) {
			if (repasse.isVencido() || repasse.isVencendo()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Transient
	public BigDecimal getReceita() {
		if (isPagamentoSomenteJuros()) {
			BigDecimal taxa = getTaxa().divide(new BigDecimal("100"));
			return getValor().multiply(taxa);
		} else {
			AtomicReference<BigDecimal> somatorioDasParcelas = new AtomicReference<BigDecimal>(BigDecimal.ZERO);
			this.getParcelas().forEach( parcela -> somatorioDasParcelas.set(somatorioDasParcelas.get().add(parcela.getValor())));
			return somatorioDasParcelas.get().subtract(getValor());
		}
	}
	
	@Transient
	public BigDecimal getValorSomenteJuros() {
		BigDecimal taxa = getTaxa().divide(new BigDecimal("100"));
		return getValor().multiply(taxa);
	}
	
	@Transient
	public BigDecimal getValorSomenteJurosDeRepasse() {
		BigDecimal taxa = getTaxaDeRepasse().divide(new BigDecimal("100"));
		return getValor().multiply(taxa);
	}	
	
	@Transient
	public BigDecimal getReceitaDeRepasse() {
		if (!isRepasse()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal quantidadeDeRepasses = new BigDecimal(getRepassesMensais().size());
		BigDecimal taxa = getTaxaDeRepasse().divide(new BigDecimal("100"));
		return getValor().multiply(quantidadeDeRepasses.multiply(taxa));
	}
	
	@Transient
	public BigDecimal getReceitaDeRepasse(Integer quantidadeDeRepasses) {
		BigDecimal taxa = getTaxaDeRepasse().divide(new BigDecimal("100"));
		return getValor().multiply(new BigDecimal(quantidadeDeRepasses).multiply(taxa));
	}
	
	@Transient
	public LocalDate getDataQuitacao() {
		return getParcelas().stream().reduce((a,b) -> b).map(parcela -> parcela.getDataDoPagamento()).orElse(null);
	}
	
	@Transient
	public boolean isPodeFinalizar() {
		return !isCancelada() && getDataFinalizacao() == null && getDataQuitacao() != null &&
				getRepassesMensais().stream().noneMatch(repasse -> repasse.getDataDoPagamento() == null);
	}
	
	@Transient
	public boolean isCancelada() {
		return getDataDeCancelamento() != null;
	}	
	
	@Transient
	@JsonIgnore
	public void historico(String historico) {
		historico = (getHistorico() == null ? "" : getHistorico()) + historico;
		setHistorico(historico + System.getProperty("line.separator"));
	}	

	@Transient
	public ContaCorrente getContaCorrentePagamentoSomenteJuros() {
		return contaCorrentePagamentoSomenteJuros;
	}

	public void setContaCorrentePagamentoSomenteJuros(ContaCorrente contaCorrentePagamentoSomenteJuros) {
		this.contaCorrentePagamentoSomenteJuros = contaCorrentePagamentoSomenteJuros;
	}

	@Transient
	public ContaCorrente getContaCorrentePagamentoDoRepasseSomenteJuros() {
		return contaCorrentePagamentoDoRepasseSomenteJuros;
	}

	public void setContaCorrentePagamentoDoRepasseSomenteJuros(ContaCorrente contaCorrentePagamentoDoRepasseSomenteJuros) {
		this.contaCorrentePagamentoDoRepasseSomenteJuros = contaCorrentePagamentoDoRepasseSomenteJuros;
	}

	@Transient
	public LocalDate getDataDePagamentoSomenteJuros() {
		return dataDePagamentoSomenteJuros;
	}

	public void setDataDePagamentoSomenteJuros(LocalDate dataDePagamentoSomenteJuros) {
		this.dataDePagamentoSomenteJuros = dataDePagamentoSomenteJuros;
	}

	//GETs e SETs
	public Integer getNumero() {
		return numero;
	}
	
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalDateTime getDataDeAutorizacao() {
		return dataDeAutorizacao;
	}

	public void setDataDeAutorizacao(LocalDateTime dataDeAutorizacao) {
		this.dataDeAutorizacao = dataDeAutorizacao;
	}

	public LocalDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public LocalDate getDataDeCancelamento() {
		return dataDeCancelamento;
	}

	public void setDataDeCancelamento(LocalDate dataDeCancelamento) {
		this.dataDeCancelamento = dataDeCancelamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTaxa() {
		return taxa;
	}

	public void setTaxa(BigDecimal taxa) {
		this.taxa = taxa;
	}

	public Integer getQuantidadeDeParcelas() {
		return quantidadeDeParcelas;
	}

	public void setQuantidadeDeParcelas(Integer quantidadeDeParcelas) {
		this.quantidadeDeParcelas = quantidadeDeParcelas;
	}

	public Investidor getInvestidor() {
		return investidor;
	}

	public void setInvestidor(Investidor investidor) {
		this.investidor = investidor;
	}

	public TipoDeOperacao getTipoDeOperacao() {
		return tipoDeOperacao;
	}

	public void setTipoDeOperacao(TipoDeOperacao tipoDeOperacao) {
		this.tipoDeOperacao = tipoDeOperacao;
	}

	public Investidor getInvestidorSenior() {
		return investidorSenior;
	}

	public void setInvestidorSenior(Investidor investidorSenior) {
		this.investidorSenior = investidorSenior;
	}

	public BigDecimal getTaxaDeRepasse() {
		return taxaDeRepasse;
	}

	public void setTaxaDeRepasse(BigDecimal taxaDeRepasse) {
		this.taxaDeRepasse = taxaDeRepasse;
	}

	public TipoDePagamento getTipoDePagamento() {
		return tipoDePagamento;
	}

	public void setTipoDePagamento(TipoDePagamento tipoDePagamento) {
		this.tipoDePagamento = tipoDePagamento;
	}

	public UserApplication getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(UserApplication responsavel) {
		this.responsavel = responsavel;
	}

	public Transferencia getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Transferencia transferencia) {
		this.transferencia = transferencia;
	}

	public List<Parcela> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<Parcela> parcelas) {
		this.parcelas = parcelas;
	}
	
	public List<RepasseMensal> getRepassesMensais() {
		return repassesMensais;
	}

	public void setRepassesMensais(List<RepasseMensal> repassesMensais) {
		this.repassesMensais = repassesMensais;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}
}