package br.com.rmc.cepf.contabil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoContabilRepository extends
	JpaRepository<LancamentoContabil, Long>, JpaSpecificationExecutor<LancamentoContabil> {
}
