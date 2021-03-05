package br.com.rmc.cepf.conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;
import br.com.rmc.security.user.User;

@Entity
@Table(schema="financeiro")
public class Transferencia extends BaseEntity {
	
	private static final long serialVersionUID = -7988472959761323479L;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDateTime data;
	
	@NotNull
	@JoinColumn(nullable = false)
	@OneToOne(cascade = CascadeType.ALL)
	private Favorecido favorecido;
	
	@NotNull
    @OneToOne
	@JoinColumn(nullable = false)
	private ContaCorrente contaOrigem;
	
	@NotNull
	@Column(scale = 2, nullable = false)
	private BigDecimal valor;
	
	@Column(scale = 2)
	private BigDecimal tarifa;
	
	@NotNull
	@OneToOne
	@JoinColumn(nullable = false)	
	private User responsavel;	

	//GETs e SETs	
	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Favorecido getFavorecido() {
		return favorecido;
	}

	public void setFavorecido(Favorecido favorecido) {
		this.favorecido = favorecido;
	}

	public ContaCorrente getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(ContaCorrente contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTarifa() {
		return tarifa;
	}

	public void setTarifa(BigDecimal tarifa) {
		this.tarifa = tarifa;
	}

	public User getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(User responsavel) {
		this.responsavel = responsavel;
	}
}