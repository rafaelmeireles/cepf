package br.com.rmc.cepf.contabil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaContabilRepository extends JpaRepository<ContaContabil, Long> {
}
