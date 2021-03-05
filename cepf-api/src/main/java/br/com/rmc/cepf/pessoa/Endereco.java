package br.com.rmc.cepf.pessoa;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
class Endereco {
	
	@Column(name = "tipo_logradouro")
	@Enumerated(EnumType.STRING)
	private TipoLogradouro tipoLogradouro;
	
	private String logradouro;
	private Integer numero;
	private String complemento;
	private String bairro;
	private String cidade;
	
	@Enumerated(EnumType.STRING)
	private Uf uf;
	
	private String cep;
	
	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}
	
	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public String getLogradouro() {
		return logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	
	public Integer getNumero() {
		return numero;
	}
	
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public String getComplemento() {
		return complemento;
	}
	
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public Uf getUf() {
		return uf;
	}
	
	public void setUf(Uf uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}
	
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Override
	public String toString() {		
		return getTipoLogradouro().getAbreviacao() + " " + getLogradouro() + 
				", " +getNumero()  + (getComplemento() != null ? " " + getComplemento() : "") +
				", " + getBairro() + ", " + getCidade() +
				", " + getUf() + " CEP: " + getCep();
	}
}