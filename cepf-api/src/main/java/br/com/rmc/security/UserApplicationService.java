package br.com.rmc.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rmc.BaseException;
import br.com.rmc.cepf.investidor.Investidor;
import br.com.rmc.cepf.investidor.InvestidorRepository;
import br.com.rmc.security.user.UserService;

@Service
public class UserApplicationService extends UserService<UserApplication> {
	
	private static final long serialVersionUID = 4044055203125228964L;
	
	@Autowired
	private InvestidorRepository investidorRepository;
	
	public UserApplicationService(UserApplicationRepository repository) {
		super(repository);
	}
	
	@Override
	public UserApplication persist(UserApplication user) {
		
		Optional<Investidor> investidor = investidorRepository.findByCpf(user.getCpf());
		
		if (!investidor.isPresent()) {
			throw new BaseException("O investidor informado não existe.");
		}
		
		return super.persist(user);
	}
}
