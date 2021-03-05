package br.com.rmc.cepf.cliente;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;
import br.com.rmc.cepf.contabil.ContaContabil;
import br.com.rmc.cepf.investidor.Investidor;
import br.com.rmc.cepf.pessoa.Pessoa;

@Entity
@Table(schema="cadastro")
public class Cliente extends BaseEntity {
	
	private static final long serialVersionUID = 4651329881791794502L;
	
	private Double taxa;
	
	@NotNull
	@JoinColumn(nullable = false)
	@OneToOne(cascade = CascadeType.PERSIST)	
	private Pessoa pessoa;
	
	@JoinColumn
	@OneToOne(cascade = CascadeType.ALL)
	private Referencia referencia;
	
	@NotNull
	@OneToOne
	@JoinColumn(nullable = false)
	private Investidor investidor;
	
	@NotNull
	@JoinColumn(nullable = false)
	@OneToOne(cascade = CascadeType.ALL)
	private ContaContabil conta;
	
	@Transient
	public String getLinkWhatsApp() {
		String mensagem = "Olá%20bom%20dia,%20temos%20vencimento%20pra%20hj.%20Confirma%20pagamento?";
		return "https://api.whatsapp.com/send?phone=55" + getPessoa().getTelefonePrincipal() + "&text=" + mensagem;
	}

	//GETs e SETs
	public Double getTaxa() {
		return taxa;
	}

	public void setTaxa(Double taxa) {
		this.taxa = taxa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Referencia getReferencia() {
		return referencia;
	}

	public void setReferencia(Referencia referencia) {
		this.referencia = referencia;
	}

	public Investidor getInvestidor() {
		return investidor;
	}

	public void setInvestidor(Investidor investidor) {
		this.investidor = investidor;
	}

	public ContaContabil getConta() {
		return conta;
	}

	public void setConta(ContaContabil contaContabil) {
		this.conta = contaContabil;
	}
}