package br.com.rmc.cepf.cliente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;

@Entity
@Table(schema="cadastro")
public class Referencia extends BaseEntity {
	
	private static final long serialVersionUID = 4651329881791794502L;
	
	@NotNull
	@Column(nullable = false)	
	private String nome;
	
	@NotNull
	@Column(nullable = false)	
	private String telefone;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
}