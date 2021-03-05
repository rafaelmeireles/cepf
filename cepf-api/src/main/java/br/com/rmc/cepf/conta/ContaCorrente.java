package br.com.rmc.cepf.conta;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.rmc.BaseEntity;
import br.com.rmc.cepf.contabil.ContaContabil;
import br.com.rmc.cepf.investidor.Investidor;

@Entity
@Table(schema="financeiro", name = "conta_corrente")
//@JsonIdentityInfo(scope = ContaCorrente.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ContaCorrente extends BaseEntity {
	
	private static final long serialVersionUID = -7988472959761323479L;
	
	@NotNull
    @OneToOne
	@JoinColumn(nullable = false)	
	private Banco banco;
	
	@NotNull
	@Column(nullable = false)
	private Integer agencia;
	
	@NotNull
	@Column(nullable = false)
	private Integer digitoAgencia;
	
	@NotNull
	@Column(nullable = false)
	private Integer conta;
	
	@NotNull
	@Column(nullable = false)
	private Integer digitoConta;	
	
	@NotNull
	@Column(nullable = false, unique = true)
	private String descricao;
	
	@NotNull
	@JoinColumn(nullable = false)
	@OneToOne(cascade = CascadeType.ALL)
	private ContaContabil contaContabil;
	
	@OneToOne
//	@JsonIgnore
	@JsonProperty(access = Access.WRITE_ONLY)
	@JoinColumn(name = "investidor_id", insertable = false, updatable = false)
	private Investidor investidor;
	
	public void descricao(String cliente) {
		setDescricao("Conta Corrente - " + cliente + " - " + getBanco().getNome() + " - " + getConta() + getDigitoConta().toString());
	}
	
	//GETs e SETs
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Integer getAgencia() {
		return agencia;
	}

	public void setAgencia(Integer agencia) {
		this.agencia = agencia;
	}

	public Integer getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(Integer digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public Integer getConta() {
		return conta;
	}

	public void setConta(Integer conta) {
		this.conta = conta;
	}

	public Integer getDigitoConta() {
		return digitoConta;
	}

	public void setDigitoConta(Integer digitoConta) {
		this.digitoConta = digitoConta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	public Investidor getInvestidor() {
		return investidor;
	}

	public void setInvestidor(Investidor investidor) {
		this.investidor = investidor;
	}
}