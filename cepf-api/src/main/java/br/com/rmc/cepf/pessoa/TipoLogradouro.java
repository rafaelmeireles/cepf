package br.com.rmc.cepf.pessoa;

public enum TipoLogradouro {
		
	ALAMEDA("AL."), AVENIDA("AV."), QUADRA("Q."), RUA("R.");
	
	private String abreviacao;	
	
	private TipoLogradouro(String abreviacao) {
		this.abreviacao = abreviacao;
	}
	
	public String getAbreviacao() {
		return this.abreviacao;
	}
}
