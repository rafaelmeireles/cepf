package br.com.rmc.cepf.conta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;

@Entity
@Table(schema="financeiro")
public class Banco extends BaseEntity {
	
	private static final long serialVersionUID = -7988472959761323479L;
	
	@NotNull
	@Column(nullable = false, unique = true)
	private Integer codigo;	
	
	@NotNull
	@Column(nullable = false, unique = true)
	private String nome;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}	
}