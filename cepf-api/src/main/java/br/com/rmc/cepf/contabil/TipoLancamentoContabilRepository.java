package br.com.rmc.cepf.contabil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLancamentoContabilRepository extends JpaRepository<TipoLancamentoContabil, Long> {
}
