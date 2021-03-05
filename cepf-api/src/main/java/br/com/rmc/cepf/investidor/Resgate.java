package br.com.rmc.cepf.investidor;

import java.math.BigDecimal;

import br.com.rmc.BaseEntity;
import br.com.rmc.cepf.conta.ContaCorrente;

public class Resgate extends BaseEntity {
	
	private static final long serialVersionUID = 3295510281447502854L;

	private Investidor investidor;
	private Investidor investidorSenior;
	private BigDecimal valor;
	private ContaCorrente contaCorrente;
	
	//GETs e SETs
	public Investidor getInvestidor() {
		return investidor;
	}
	
	public void setInvestidor(Investidor investidor) {
		this.investidor = investidor;
	}
	
	public Investidor getInvestidorSenior() {
		return investidorSenior;
	}

	public void setInvestidorSenior(Investidor investidorSenior) {
		this.investidorSenior = investidorSenior;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public ContaCorrente getContaCorrente() {
		return contaCorrente;
	}
	
	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}
}