package br.com.rmc.cepf.conta;

import org.springframework.stereotype.Service;

import br.com.rmc.BaseService;

@Service
public class BancoService extends BaseService<Banco> {
	
	private static final long serialVersionUID = -681678049203666599L;

	public BancoService(BancoRepository repository) {
		super(repository);
	}
}
