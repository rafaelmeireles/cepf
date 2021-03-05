package br.com.rmc.security;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.rmc.cepf.investidor.Investidor;
import br.com.rmc.security.user.User;

@Entity
//@JsonIdentityInfo(scope = UserApplication.class, generator = ObjectIdGenerators.StringIdGenerator.class)
public class UserApplication extends User {

	private static final long serialVersionUID = -5017824520579753974L;

	@OneToOne
	@JoinColumn(unique = true)
//	@JsonIdentityInfo(scope = Investidor.class, generator = ObjectIdGenerators.StringIdGenerator.class)
	private Investidor investidor;

	public Investidor getInvestidor() {
		return investidor;
	}

	public void setInvestidor(Investidor investidor) {
		this.investidor = investidor;
	}

}