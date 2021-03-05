package br.com.rmc.cepf.conta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
}
