package br.com.rmc.cepf.pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.rmc.BaseEntity;

@Entity
@Table(schema="cadastro")
public class Pessoa extends BaseEntity {
	
	private static final long serialVersionUID = 7223108297848873298L;
	
	@Size(min = 11, max = 11)
	@Column(unique = true)	
	private String cpf;
	
	@NotNull
	@Column(nullable = false)	
	private String nome;
	
	private Endereco endereco;
	
	@NotNull
	@Column(name = "telefone_principal", nullable = false)	
	private String telefonePrincipal;
	
	@Column(name = "telefone_secundario")	
	private String telefoneSecundario;
	
	@Column(unique = true)
	@Email(message = "Falha na validação do campo email. Informe um valor válido.")
	private String email;
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public String getTelefonePrincipal() {
		return telefonePrincipal;
	}
	
	public void setTelefonePrincipal(String telefonePrincipal) {
		this.telefonePrincipal = telefonePrincipal;
	}
		
	public String getTelefoneSecundario() {
		return telefoneSecundario;
	}
	
	public void setTelefoneSecundario(String telefoneSecundario) {
		this.telefoneSecundario = telefoneSecundario;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}