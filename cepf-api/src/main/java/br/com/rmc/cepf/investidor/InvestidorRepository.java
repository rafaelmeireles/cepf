package br.com.rmc.cepf.investidor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestidorRepository extends JpaRepository<Investidor, Long> {
	
	@Query("select i from Investidor i where i.pessoa.cpf = :cpf")
	public Optional<Investidor> findByCpf(@Param("cpf") String cpf);
}
