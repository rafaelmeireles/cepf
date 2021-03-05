package br.com.rmc.cepf.contabil;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;
import br.com.rmc.BaseException;
import br.com.rmc.Util;

@Entity
@Table(schema="contabil", name = "conta_contabil")
public class ContaContabil extends BaseEntity {
	
	private static final long serialVersionUID = 8965694912326577399L;
	
	@NotNull
	@Column(nullable = false, unique = true)
	private String nome;
	
	@NotNull
	@Column(nullable = false, scale = 2)
	private BigDecimal saldo;
	
	private Boolean credora;
	
	private Boolean devedora;
	
	@Transient
	public Boolean isCredora() {
		return getCredora() != null && getCredora();
	}
	
	@Transient
	public Boolean isDevedora() {
		return getDevedora() != null && getDevedora();
	}
	
	@Override
	public void validate() throws BaseException {
		if (getCredora() == null && getDevedora() == null) {
			throw new BaseException("Não foi informado se a conta é de natureza credora ou devedora.");
		}

		if (getCredora() != null && getDevedora() != null) {
			throw new BaseException("A conta precisa ser de natureza credora ou devedora.");
		}
	}
	
	public ContaContabil nome(String nome) {
		setNome(nome);
		return this;
	}
	
	public ContaContabil credora() {
		setCredora(true);
		setDevedora(false);
		return this;
	}
	
	public ContaContabil devedora() {
		setDevedora(true);
		setCredora(false);
		return this;
	}

	public ContaContabil zeraSaldo() {
		setSaldo(BigDecimal.ZERO);
		return this;
	}
	
	public void creditar(BigDecimal valor) {
		setSaldo(getSaldo().add(valor));
	}
	
	public void debitar(BigDecimal valor) {
//		if (valor.compareTo(getSaldo()) == 1) {
//			throw new BaseException(
//					"Saldo insuficiente na conta " + getNome() + 
//					". Saldo atual " + Util.formatMoney(getSaldo()) + ", valor do lançamento " + Util.formatMoney(valor) + ".");
//		}
//		
		setSaldo(getSaldo().subtract(valor));
	}
	
	//GETs e SETs
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Boolean getCredora() {
		return credora;
	}

	public void setCredora(Boolean credora) {
		this.credora = credora;
	}

	public Boolean getDevedora() {
		return devedora;
	}

	public void setDevedora(Boolean devedora) {
		this.devedora = devedora;
	}	
}