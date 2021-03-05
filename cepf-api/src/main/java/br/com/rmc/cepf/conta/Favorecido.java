package br.com.rmc.cepf.conta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.rmc.BaseEntity;
import br.com.rmc.cepf.cliente.Cliente;

@Entity
@Table(schema="financeiro")
public class Favorecido extends BaseEntity {
	
	private static final long serialVersionUID = -7988472959761323479L;
	
	@NotNull
    @OneToOne
	@JoinColumn(nullable = false)
	private Banco banco;
	
	@NotNull
	@Column(nullable = false)
	private Integer agencia;
	
	@Column(name = "digito_agencia")
	private Integer digitoAgencia;
	
	@NotNull
	@Column(nullable = false)
	private Integer conta;
	
	@NotNull
	@Column(name = "digito_conta", nullable = false)
	private Integer digitoConta;	
	
	@NotNull
	@Column(nullable = false)
	private String nome;
	
	@Column
	@Size(min = 11, max = 11)
	private String cpf;
	
	@Column
	@Size(min = 14, max = 14)
	private String cnpj;	
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Cliente cliente;

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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}