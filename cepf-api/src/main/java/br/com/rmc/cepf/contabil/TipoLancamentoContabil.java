package br.com.rmc.cepf.contabil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;

@Entity
@Table(schema="contabil", name = "tipo_lancamento_contabil")
public class TipoLancamentoContabil extends BaseEntity {
	
	private static final long serialVersionUID = -7988472959761323479L;
	
	public TipoLancamentoContabil() {
	}
	
	public TipoLancamentoContabil(String nome) {
		this.nome = nome;
	}	
	
	@NotNull
	@Column(nullable = false, unique = true)
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}	
}