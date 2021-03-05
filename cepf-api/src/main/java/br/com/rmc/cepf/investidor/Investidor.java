package br.com.rmc.cepf.investidor;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;
import br.com.rmc.cepf.conta.ContaCorrente;
import br.com.rmc.cepf.pessoa.Pessoa;

@Entity
@Table(schema="cadastro")
//@JsonIdentityInfo(scope = Investidor.class, generator = ObjectIdGenerators.StringIdGenerator.class)
//@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Investidor extends BaseEntity {
	
	private static final long serialVersionUID = 3295510281447502854L;
	
//	@JsonIgnoreProperties("investidor")
	@NotNull
	@JoinColumn(nullable = false)
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private Pessoa pessoa;
	
	@JoinColumn(nullable = false, name = "investidor_id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ContaCorrente> contas;
	
	//GETs e SETs	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<ContaCorrente> getContas() {
		return contas;
	}

	public void setContas(List<ContaCorrente> contas) {
		this.contas = contas;
	}
}