package br.com.rmc.cepf.investidor;

import java.time.LocalDate;

import br.com.rmc.BaseEntity;

public class ApuracaoResultado extends BaseEntity {
	
	private static final long serialVersionUID = 3295510281447502854L;

	private LocalDate periodoInicial;
	private LocalDate periodoFinal;
	private Investidor investidor;
	
	//GETs e SETs
	public LocalDate getPeriodoInicial() {
		return periodoInicial;
	}
	
	public void setPeriodoInicial(LocalDate periodoInicial) {
		this.periodoInicial = periodoInicial;
	}
	
	public LocalDate getPeriodoFinal() {
		return periodoFinal;
	}
	
	public void setPeriodoFinal(LocalDate periodoFinal) {
		this.periodoFinal = periodoFinal;
	}

	public Investidor getInvestidor() {
		return investidor;
	}

	public void setInvestidor(Investidor investidor) {
		this.investidor = investidor;
	}
}