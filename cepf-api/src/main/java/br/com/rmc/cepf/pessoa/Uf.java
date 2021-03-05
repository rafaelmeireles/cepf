package br.com.rmc.cepf.pessoa;


/**
 * Enum de estados da federacao.
 * @author locaLisa (Usa quem precisa)
 */

public enum Uf {

	AC("ACRE"),
	AL("ALAGOAS"),
	AM("AMAZONAS"),
	AP("AMAPA"),
	BA("BAHIA"),
	CE("CEARÁ"),
	DF("DISTRITO FEDERAL"),
	ES("ESPIRITO SANTO"),
	GO("GOIAS"),
	MA("MARANHÃO"),
	MG("MINAS GERAIS"),
	MT("MATO GROSSO"),
	MS("MATO GROSSO DO SUL"),
	PA("PARA"),
	PB("PARAÍBA"),
	PE("PERNAMBUCO"),
	PI("PIAUÍ"),
	PR("PARANÁ"),
	RJ("RIO DE JANEIRO"),
	RN("RIO GRANDE DO NORTE"),
	RO("RONDÔNIA"),
	RR("RORAIMA"),
	RS("RIO GRANDE DO SUL"),
	SC("SANTA CATARINA"),
	SE("SERGIPE"),
	SP("SÃO PAULO"),
	TO("TOCANTINS");

	private String nome;

	/**
	 * Construtor do enum, com parametro nomeExtenso.
	 * @param nome String - O nome por extenso do enum
	 */
	private Uf(String nome) {
		this.nome = nome;
	}

	/**
	 * Retorna o nome em extenso do enum.
	 * @return String - O nome por extenso
	 */
	public String getNome() {
		return nome;
	}

}