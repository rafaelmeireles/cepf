package br.com.rmc.cepf.pessoa;

import org.springframework.stereotype.Service;

import br.com.rmc.BaseService;

@Service
class PessoaService extends BaseService<Pessoa> {
	
	private static final long serialVersionUID = 4044055203125228964L;
	
	public PessoaService(PessoaRepository repository) {
		super(repository);
	}
}
