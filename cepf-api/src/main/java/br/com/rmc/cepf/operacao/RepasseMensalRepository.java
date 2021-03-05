package br.com.rmc.cepf.operacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RepasseMensalRepository extends JpaRepository<RepasseMensal, Long>, JpaSpecificationExecutor<RepasseMensal>  {
}
