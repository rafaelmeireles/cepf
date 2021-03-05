package br.com.rmc.cepf.operacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long>, JpaSpecificationExecutor<Parcela>  {
	
//	@Override
//	default List<Parcela> findAll(Specification<Parcela> specification) {
//		
//		Assert.isTrue(dataInicial != null || dataFinal != null, "Pelo menos uma das datas deve ser informada.");
//		
//		return getRepository().findAll(
//				Specification
//				.where(new JoinSpecification<Operacao, Cliente>("cliente", operacao.getCliente()))
//				.and(new JoinSpecification<Operacao, Investidor>("investidor", operacao.getInvestidor()))
//				.and(new JoinSpecification<Operacao, Investidor>("investidorSenior", operacao.getInvestidorSenior()))
//				.and(new JoinSpecification<Operacao, UserApplication>("responsavel", operacao.getResponsavel()))
//				.and(new OrSpecification<Operacao>("tipoDeOperacao", tiposOperacao))
//				.and(new OrSpecification<Operacao>("tipoDePagamento", tiposPagamento))
//				.and(new DateSpecification<Operacao>("data", dataInicial, dataFinal)),
//				Sort.by(Direction.ASC, "data")
//				);		
//		return null;
//	}
}
