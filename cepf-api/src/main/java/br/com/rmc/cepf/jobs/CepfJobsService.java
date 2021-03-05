package br.com.rmc.cepf.jobs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.rmc.Util;
import br.com.rmc.cepf.investidor.Investidor;
import br.com.rmc.cepf.investidor.InvestidorRepository;
import br.com.rmc.cepf.operacao.Parcela;
import br.com.rmc.cepf.operacao.ParcelaRepository;
import br.com.rmc.service.EmailService;
import br.com.rmc.specification.DateSpecification;
import br.com.rmc.specification.IsNullSpecification;

@Service
public class CepfJobsService {
	
	@Autowired
	private ParcelaRepository parcelaRepository;
	
	@Autowired
	private InvestidorRepository investidorRepository;
	
	@Autowired
	private EmailService emailService;

	@Scheduled(cron = "0 10 0 * * *")//todo dia a 00:10:00
//	@Scheduled(cron = "0 30 13 * * *")
	public void cobrancasDoDia() {
		List<Investidor> investidores = this.investidorRepository.findAll();
		
		investidores.forEach(investidor -> {
			this.notificarInvestidor(investidor);
		});
	}
	
	public void notificarInvestidor(Investidor investidor) {
		
		List<Parcela> parcelas = new ArrayList<Parcela>();		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dataDeVencimentoFinal = LocalDate.parse(LocalDate.now().format(formatter), formatter);		
		
		parcelas = parcelaRepository.findAll(
		Specification
			.where(new DateSpecification<Parcela>("dataDeVencimento", null, dataDeVencimentoFinal))
			.and(new IsNullSpecification<Parcela>("dataDoPagamento")),
			Sort.by(Direction.ASC, "dataDeVencimento", "dataDeProrrogacao"));
		
		parcelas.removeIf(parcela -> !parcela.getOperacao().getInvestidor().equals(investidor));
		parcelas.removeIf(parcela -> parcela.getOperacao().getDataDeAutorizacao() == null);
		parcelas.removeIf(parcela -> parcela.getDataDeProrrogacao() != null && parcela.getDataDeProrrogacao().isAfter(dataDeVencimentoFinal));
		
		StringBuilder mailMessage = new StringBuilder("Bom dia " + investidor.getPessoa().getNome() +". Segue lista de parcelas vencidas e/ou vencendo:");
		
		parcelas.forEach(parcela -> {
			mailMessage.append("\n\n\tCliente - " + parcela.getCliente().getPessoa().getNome());
			mailMessage.append("\n\tOperação " + parcela.getOperacao().getNumero() + " - " + Util.formatLocalDateToString(parcela.getOperacao().getData()) + " - " + Util.formatMoney(parcela.getOperacao().getValor()) + " - " + parcela.getOperacao().getTaxa() + "%");
			mailMessage.append("\n\tParcela " + parcela.getNumero() + " - " + Util.formatLocalDateToString(parcela.getVencimentoAtual()) + " - " + Util.formatMoney(parcela.getValor()));
			mailMessage.append("\n\t" + parcela.getCliente().getLinkWhatsApp());			
		});
		
		if (!parcelas.isEmpty()) {
			this.emailService.send(investidor.getPessoa().getEmail(), "[CEPF - NOTIFICAÇÃO] - Cobranças do dia para - " + investidor.getPessoa().getNome(), mailMessage.toString());
		}
	}
}
